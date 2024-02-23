import {useEffect, useState} from "react";
import {BattleshipDetails} from "../../../types/Battleship.ts";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AppUser} from "../../../types/AppUser.ts";

type Props = {
    appUser: AppUser | null
}

export default function BattleshipGameCard(props: Readonly<Props>) {


    const [game, setGame] = useState<BattleshipDetails>();

    const {id} = useParams<{ id: string }>();

    useEffect(() => {
        if (id === undefined) return;
        axios.get('/api/games/battleships/' + id)
            .then(response => setGame(response.data))
    }, [id]);

    if (game === undefined) {
        return <div>Loading...</div>
    }

    const makeTurn = (position: { x: number, y: number, targetPlayerId: string }) => {
        axios.post('/api/games/battleships/' + id + '/turn', position)
            .then(response => setGame(response.data))
    }

    return (
        <div>
            <p>My Board</p>
            {props.appUser && <BattleshipBoardCard board={game.boards[props.appUser.id]}
                                                   setup={false}
                                                   onShipSelect={() => {
                                                   }}
                                                   onFieldClick={() => {
                                                   }}/>
            }

            <p>Enemy Board</p>
            {Object.keys(game.boards).filter(key => key !== props.appUser?.id.toString()).map(key => (
                <BattleshipBoardCard key={key}
                                     board={game.boards[key]}
                                     setup={false}
                                     onShipSelect={() => {
                                     }}
                                     onFieldClick={(position) => makeTurn({...position, targetPlayerId: key})}/>))}
        </div>
    );
}
