import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import BattleshipShipSelection from "./BattleshipShipSelection.tsx";
import {useState} from "react";
import {
    BattleshipCreation,
    BattleshipShip,
    defaultBattleshipBoard,
    defaultBattleshipConfig
} from "../../../types/Battleship.ts";
import {shipLengths} from "./BattleshipShipCard.tsx";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type ShipPositions = { ship: BattleshipShip, position?: { x: number, y: number } }[]


export default function BattleshipCreateView() {

    const [shipPositions, setShipPositions] = useState<ShipPositions>(defaultBattleshipConfig.availableShipsPerPlayer.map(ship => ({ship})));
    const [game, setGame] = useState<BattleshipCreation>({
        board: defaultBattleshipBoard
    })
    const navigate = useNavigate()

    const updateGameFromShipPositions = (newShipPositions: ShipPositions) => {

        function getCellValue(rowIndex: number, columnIndex: number, shipPositions: ShipPositions, shipLengths: {
            CARRIER: number,
            BATTLESHIP: number,
            CRUISER: number,
            DESTROYER: number
        }) {
            const shipPosition = shipPositions.find(({position, ship}) =>
                position?.y === rowIndex &&
                position.x <= columnIndex &&
                position.x + shipLengths[ship] > columnIndex
            );
            return shipPosition ? "SHIP" : "EMPTY";
        }

        setGame({
            ...game, board: game.board
                .map((row, rowIndex) => row
                    .map((_, columnIndex) => getCellValue(rowIndex, columnIndex, newShipPositions, shipLengths))
                )
        });
    }

    const onShipPlaced = (ship: BattleshipShip, position: { x: number, y: number }) => {

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
                                         onShipSelect={onShipPlaced}
                                         onFieldClick={removeShip}/>
                    <BattleshipShipSelection
                        availableShips={shipPositions.filter(sp => sp.position === undefined).map(sp => sp.ship)}/>
                </div>
            </DndProvider>
            <button onClick={startBattleshipGameClick} className="btn btn-outline-light">Start</button>
        </div>
    )
}
