import {BattleshipField, BattleshipShip} from "../../../types/Battleship.ts";
import {useDrop} from "react-dnd";

type Props = {
    field: BattleshipField;
    onShipSelect: (ship: BattleshipShip, position: {x: number, y: number}) => void;
    position: {x: number, y: number};
}
export default function BattleshipFieldCard(props: Props) {

    const [{isOver}, drop] = useDrop<{ship: BattleshipShip}, void, {isOver: boolean, canDrop: boolean}>(() => ({
        accept: "battleshipShip",
        drop: (item) => {
            props.onShipSelect(item.ship, props.position)
        },
        collect: monitor => ({
            isOver: monitor.isOver(),
            canDrop: monitor.canDrop(),
        }),
    }), [props.field, props.position, props.onShipSelect])


const css = isOver ? "col border border-success" : "col border";
    return <div ref={drop} className={css}>
        {props.field}
    </div>;
}