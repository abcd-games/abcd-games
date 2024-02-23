import {BattleshipField} from "../../../types/Battleship.ts";

type Props = {
    field: BattleshipField;
    position: { x: number, y: number };
    onClick: (position: { x: number, y: number }) => void;
}
export default function BattleshipFieldCard(props: Readonly<Props>) {


    const onFieldClick = () => {
        props.onClick(props.position)
    }


    let css = "battleship_field_card col"
    css += props.field === "EMPTY"
        ? " border"
        : ""

    css += props.field === "SHIP"
        ? " bg-warning"
        : " border"
    return <div className={css} onClick={onFieldClick} role="button">
        {props.field.charAt(0)}
    </div>;
}
