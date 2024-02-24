import {BattleshipField} from "../../../types/Battleship.ts";
import {ConnectDropTarget} from "react-dnd";

type Props = {
    field: BattleshipField;
    position: { x: number, y: number };
    onClick: (position: { x: number, y: number }) => void;
    dropTargetRef?: ConnectDropTarget
    isOver?: boolean
}
export default function BattleshipFieldCard(props: Readonly<Props>) {


    const onFieldClick = () => {
        props.onClick(props.position)
    }


    let css = "battleship_field_card col"
    if (props.isOver) {
        css += props.field === "EMPTY" ? "  border-success" : " border-danger"
    }
    if (props.field === "EMPTY") {
        css += " bg-info border"
    }
    if (props.field === "SHIP") {
        css += " bg-warning border border-warning not_empty"
    }
    if (props.field === "HIT") {
        css += " bg-danger border border-danger not_empty"
    }
    if (props.field === "MISS") {
        css += " bg-primary border not_empty"
    }

    return <button className={css} ref={props.dropTargetRef} onClick={onFieldClick} role="button"/>;
}
