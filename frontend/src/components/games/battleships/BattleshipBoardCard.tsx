import {BattleshipField, BattleshipShip} from "../../../types/BattleshipListDto.ts";
import BattleshipFieldCardDraggable from "./BattleshipFieldCardDraggable.tsx";
import BattleshipFieldCard from "./BattleshipFieldCard.tsx";

type Props = {
    board: BattleshipField[][];
    onShipSelect: (ship: BattleshipShip, position: { x: number, y: number }) => void;
    onFieldClick: (position: { x: number, y: number}) => void;
    setup: boolean
}


export default function BattleshipBoardCard(props: Props) {

    return (
        <div className="container">
            {props.setup && props.board.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((field, columnIndex) => (
                        <BattleshipFieldCardDraggable key={columnIndex}
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
                                                      onShipSelect={() => {}}
                                                      onClick={props.onFieldClick}/>
                    ))}
                </div>
            ))}
        </div>
    )
}
