import {useEffect, useState} from "react";
import {Battleship, BattleshipShip} from "../../../types/Battleship.ts";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import axios from "axios";
import {useParams} from "react-router-dom";


export default function BattleshipGameCard() {


    const [game, setGame] = useState<Battleship>();

    const {id} = useParams<{ id: string }>();

    useEffect(() => {
        if (id === undefined) return;
        axios.get('/api/games/battleships/' + id)
            .then(response => setGame(response.data))
    }, [id]);

    if (game === undefined) {
        return <div>Loading...</div>
    }

    const onShipSelect = (ship: BattleshipShip, position: { x: number, y: number }) => {
        console.log(ship, position);
        setGame({
            ...game, boardPlayer1: game.boardPlayer1.map((row, rowIndex) => row.map((f, columnIndex) => {
                if (rowIndex === position.y && columnIndex === position.x) {
                    return "SHIP";
                }
                return f;
            }))
        });
    }

    const makeTurn = (position: { x: number, y: number }) => {
        axios.post('/api/games/battleships/' + id + '/turn', position)
            .then(response => setGame(response.data))
    }

    return (
        <div>
            <p>My Board</p>
            <BattleshipBoardCard board={game.boardPlayer1}
                                 setup={false}
                                 onShipSelect={onShipSelect}
                                 onFieldClick={() => {
                                 }}/>

            <p>Enemy Board</p>
            <BattleshipBoardCard board={game.boardPlayer2}
                                 setup={false}
                                 onShipSelect={onShipSelect}
                                 onFieldClick={makeTurn}/>
        </div>
    );
}
