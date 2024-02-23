import {useEffect, useState} from "react";
import {BattleshipDetails} from "../../../types/BattleshipListDto.ts";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AppUser} from "../../../types/AppUser.ts";

type Props = {
    appUser: AppUser | null
}

export default function BattleshipGameCard(props: Props) {


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

    // const onShipSelect = (ship: BattleshipShip, position: { x: number, y: number }) => {
    //     console.log(ship, position);
    //     // setGame({
    //     //     ...game, boards: {
    //     //         ...game.boards,
    //     //         Object.keys(game.boards).filter game.map((row, rowIndex) => row.map((f, columnIndex) => {
    //     //         if (rowIndex === position.y && columnIndex === position.x) {
    //     //             return "SHIP";
    //     //         }
    //     //         return f;
    //     //     }))}
    //     // });
    // }

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
                <BattleshipBoardCard board={game.boards[key]}
                                     setup={false}
                                     onShipSelect={() => {}}
                                     onFieldClick={(position) => makeTurn({...position, targetPlayerId: key})}/>))}
        </div>
    );
}
