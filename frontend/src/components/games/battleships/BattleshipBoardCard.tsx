import {BattleshipField, BattleshipShip} from "../../../types/Battleship.ts";
import BattleshipFieldDraggable from "./BattleshipFieldDraggable.tsx";
import BattleshipFieldCard from "./BattleshipFieldCard.tsx";

type Props = {
    board: BattleshipField[][];
    onShipSelect: (ship: BattleshipShip, position: { x: number, y: number }) => void;
    onFieldClick: (position: { x: number, y: number}) => void;
    setup: boolean
}


export default function BattleshipBoardCard(props: Readonly<Props>) {

    return (
        <div className="container">
            {props.setup && props.board.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((field, columnIndex) => (
                        <BattleshipFieldDraggable key={columnIndex}
                                                  field={field}
                                                  position={{x: columnIndex, y: rowIndex}}
                                                  onShipSelect={props.onShipSelect}
                                                  onClick={props.onFieldClick}/>
                    ))}
                </div>
            ))}
            {!props.setup && props.board.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((field, columnIndex) => (
                        <BattleshipFieldCard key={columnIndex}
                                                      field={field}
                                                      position={{x: columnIndex, y: rowIndex}}
                                                      onClick={props.onFieldClick}/>
                    ))}
                </div>
            ))}
        </div>
    )
}
