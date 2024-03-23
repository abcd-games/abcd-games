import {BattleshipField, BattleshipShip} from "../../../types/Battleship.ts";
import {useDrop} from "react-dnd";
import BattleshipFieldCard from "./BattleshipFieldCard.tsx";

type Props = {
    field: BattleshipField;
    onShipSelect: (ship: BattleshipShip, position: { x: number, y: number, alignment: "horizontal" | "vertical"}) => void;
    position: { x: number, y: number };
    onClick: (position: { x: number, y: number }) => void;
}
export default function BattleshipFieldDraggable(props: Readonly<Props>) {

    const [{isOver}, drop] = useDrop<{ ship: BattleshipShip, alignment: "horizontal" | "vertical" }, void, { isOver: boolean, canDrop: boolean}>(() => ({
        accept: "battleshipShip",
        drop: (item) => {
            props.onShipSelect(item.ship, {...props.position, alignment: item.alignment})
        },
        collect: monitor => ({
            isOver: monitor.isOver(),
            canDrop: monitor.canDrop(),
        }),
    }), [props.field, props.position, props.onShipSelect])

    return <BattleshipFieldCard field={props.field}
                                position={props.position}
                                onClick={props.onClick}
                                dropTargetRef={drop}
                                isOver={isOver}/>;
}
