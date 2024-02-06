import useBattleships from "../hooks/useBattleship.tsx";
import BattleshipBoardCard from "../components/games/battleships/BattleshipBoardCard.tsx";
import {Battleship, BattleshipShip} from "../types/Battleship.ts";
import BattleshipShipSelection from "../components/games/battleships/BattleshipShipSelection.tsx";
import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import {useState} from "react";

const exampleBattleship: Battleship = JSON.parse("{\n" +
    "    \"players\": [\n" +
    "        {\n" +
    "            \"id\": \"1\",\n" +
    "            \"displayName\": \"Florian\"\n" +
    "        },\n" +
    "        {\n" +
    "            \"id\": \"2\",\n" +
    "            \"displayName\": \"Marcell\"\n" +
    "        }\n" +
    "    ],\n" +
    "    \"boardPlayer1\": {\n" +
    "        \"fields\": [\n" +
    "            [\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\"\n" +
    "            ],\n" +
    "            [\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\"\n" +
    "            ],\n" +
    "            [\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\",\n" +
    "                \"EMPTY\"\n" +
    "            ]\n" +
    "        ]\n" +
    "    },\n" +
    "    \"availableShipsPerPlayer\": [\n" +
    "        \"CARRIER\"\n" +
    "    ],\n" +
    "    \"currentTurn\": {\n" +
    "        \"id\": \"1\",\n" +
    "        \"displayName\":\"Florian\"\n" +
    "    },\n" +
    "    \"winner\": null\n" +
    "}");

export default function BattleshipsPage() {


    const {battleships, startBattleshipGame} = useBattleships();

    const [game, setGame] = useState<Battleship>(exampleBattleship);

    function startBattleshipGameClick() {
        startBattleshipGame(game);
    }

    const onShipSelect = (ship: BattleshipShip, position: { x: number, y: number }) => {
        console.log(ship, position);
        setGame({
            ...game, boardPlayer1: {
                ...game.boardPlayer1,
                fields: game.boardPlayer1.fields.map((row, rowIndex) => row.map((f, columnIndex) => {
                    if (rowIndex === position.y && columnIndex === position.x) {
                        return "SHIP";
                    }
                    return f;
                }))
            }
        });
    }

    return (
        <div>
            <h1>Battleships</h1>

            {battleships.map(battleship => <div key={battleship.id}>{battleship.id}</div>)}

            <DndProvider backend={HTML5Backend}>
                <BattleshipBoardCard board={game.boardPlayer1} onShipSelect={onShipSelect}/>
                <BattleshipShipSelection availableShips={exampleBattleship.availableShipsPerPlayer}/>
            </DndProvider>

            <button onClick={startBattleshipGameClick} className="btn btn-outline-light">Start</button>
        </div>
    );
}