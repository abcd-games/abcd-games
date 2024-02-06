import {BattleshipShip} from "../../../types/Battleship.ts";
import {useDrag} from "react-dnd";

type Props = {
    ship: BattleshipShip;

}
export default function BattleshipShipCard(props: Props) {
    const [{isDragging}, drag] = useDrag(() => ({
        type: "battleshipShip",
        item: {ship: props.ship},
        collect: monitor => ({
            isDragging: monitor.isDragging(),
        }),
    }), [props.ship])

    const css = isDragging ? "border disabled" : "border";
    return <div ref={drag} className={css}>
        {props.ship}
    </div>;
}