import {BattleshipShip, shipLengths} from "../../../types/Battleship.ts";
import {useDrag} from "react-dnd";

type Props = {
    ship: BattleshipShip;
    alignment: "horizontal" | "vertical";
}

export default function BattleshipShipCard(props: Readonly<Props>) {


    const [{isDragging}, drag] = useDrag(() => ({
        type: "battleshipShip",
        item: {ship: props.ship, alignment: props.alignment},
        collect: monitor => ({
            isDragging: monitor.isDragging(),
        }),
    }), [props.ship])

    let css = "border battleship_ship_selection_ship_" + props.alignment;
    css += isDragging ? " disabled" : "";

    return <div ref={drag} className={css}>
        {
            Array(shipLengths[props.ship])
                .fill(props.ship)
                .map((_, index) => <div key={index} className="border">{props.ship}</div>
                )}
    </div>;
}
