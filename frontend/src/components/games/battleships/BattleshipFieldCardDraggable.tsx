import {BattleshipField, BattleshipShip} from "../../../types/Battleship.ts";
import {useDrop} from "react-dnd";

type Props = {
    field: BattleshipField;
    onShipSelect: (ship: BattleshipShip, position: { x: number, y: number }) => void;
    position: { x: number, y: number };
    onClick: (position: { x: number, y: number }) => void;
}
export default function BattleshipFieldCardDraggable(props: Props) {

    const [{isOver}, drop] = useDrop<{ ship: BattleshipShip }, void, { isOver: boolean, canDrop: boolean }>(() => ({
        accept: "battleshipShip",
        drop: (item) => {
            props.onShipSelect(item.ship, props.position)
        },
        collect: monitor => ({
            isOver: monitor.isOver(),
            canDrop: monitor.canDrop(),
        }),
    }), [props.field, props.position, props.onShipSelect])

    const onFieldClick = () => {
        props.onClick(props.position)
    }


    let css = "battleship_field_card col"
    css += isOver
        ? props.field === "EMPTY"
            ? " border-success"
            : " border-danger"
        : ""

    css += props.field === "SHIP"
        ? " bg-warning"
        : " border"
    return <div ref={drop} className={css} onClick={onFieldClick}>
        {props.field.charAt(0)}
    </div>;
}