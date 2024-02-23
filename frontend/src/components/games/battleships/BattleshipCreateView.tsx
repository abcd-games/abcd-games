import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import BattleshipShipSelection from "./BattleshipShipSelection.tsx";
import {useState} from "react";
import {BattleshipCreation, BattleshipField, BattleshipShip} from "../../../types/BattleshipListDto.ts";
import {shipLengths} from "./BattleshipShipCard.tsx";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type ShipPositions = { ship: BattleshipShip, position?: { x: number, y: number } }[]
export const initShipPosition: ShipPositions = [
    {ship: "CARRIER", position: undefined},
    {ship: "BATTLESHIP", position: undefined},
    {ship: "BATTLESHIP", position: undefined},
    {ship: "CRUISER", position: undefined},
    {ship: "CRUISER", position: undefined},
    {ship: "CRUISER", position: undefined},
    {ship: "DESTROYER", position: undefined},
    {ship: "DESTROYER", position: undefined},
    {ship: "DESTROYER", position: undefined},
    {ship: "DESTROYER", position: undefined},
]

export const defaultBattleshipBoard: BattleshipField[][] = [
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"]]


export default function BattleshipCreateView() {

    const [shipPositions, setShipPositions] = useState<ShipPositions>(initShipPosition)
    const [game, setGame] = useState<BattleshipCreation>({
        board: defaultBattleshipBoard
    })
    const navigate = useNavigate()

    const updateGameFromShipPositions = (newShipPositions: ShipPositions) => {
        setGame({
            ...game, board: game.board
                .map((row, rowIndex) => row
                    .map((_, columnIndex) => {
                        const shipPosition = newShipPositions
                            .find(sp => sp.position?.y === rowIndex
                                && sp.position?.x <= columnIndex
                                && sp.position?.x + shipLengths[sp.ship] > columnIndex)
                        if (shipPosition !== undefined) {
                            return "SHIP";
                        } else {
                            return "EMPTY";
                        }
                    })
                )
        });
    }

    const onShipSelect = (ship: BattleshipShip, position: { x: number, y: number }) => {
        console.log(ship, position);

        if (Array(shipLengths[ship]).fill(0)
            .map((_, index) => ({x: position.x + index, y: position.y}))
            .some(p => game.board[p.y][p.x] !== "EMPTY")) {
            return;
        }
        if (game.board[position.y][position.x] !== "EMPTY") {
            return;
        }

        const newShipPositions = [...shipPositions];
        const index = newShipPositions.findIndex(sp => sp.ship === ship && sp.position === undefined);
        newShipPositions[index] = {...newShipPositions[index], position};

        setShipPositions(newShipPositions);

        updateGameFromShipPositions(newShipPositions);
    }

    const removeShip = (position: { x: number, y: number }) => {
        console.log(position)

        const newShipPositions = shipPositions.map(sp => {
            if (sp.position?.y === position.y
                && sp.position?.x <= position.x
                && sp.position?.x + shipLengths[sp.ship] > position.x) {
                return {...sp, position: undefined}
            }
            return sp;
        });

        setShipPositions(newShipPositions);
        updateGameFromShipPositions(newShipPositions);
    }

    function startBattleshipGameClick() {
        axios.post("/api/games/battleships", game)
            .then(response => navigate("/games/battleships/" + response.data.id))
    }

    return (
        <div>
            <h1>Create Battleship Game</h1>
            <DndProvider backend={HTML5Backend}>
                <div className="battleship_create_view">
                    <BattleshipBoardCard board={game.board}
                                         setup={true}
                                         onShipSelect={onShipSelect}
                                         onFieldClick={removeShip}/>
                    <BattleshipShipSelection
                        availableShips={shipPositions.filter(sp => sp.position === undefined).map(sp => sp.ship)}/>
                </div>
            </DndProvider>
            <button onClick={startBattleshipGameClick} className="btn btn-outline-light">Start</button>
        </div>
    )
}
