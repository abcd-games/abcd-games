import {BattleshipBoard, BattleshipShip} from "../../../types/Battleship.ts";
import BattleshipFieldCard from "./BattleshipFieldCard.tsx";

type Props = {
    board: BattleshipBoard;
    onShipSelect: (ship: BattleshipShip, position: {x: number, y: number}) => void;
}


export default function BattleshipBoardCard(props: Props) {

    return (
        <div className="container">
            {props.board.fields.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((field, columnIndex) => (
                        <BattleshipFieldCard key={columnIndex} field={field} onShipSelect={props.onShipSelect} position={{x: columnIndex, y: rowIndex}}/>
                    ))}
                </div>
                ))}
        </div>
    )
}