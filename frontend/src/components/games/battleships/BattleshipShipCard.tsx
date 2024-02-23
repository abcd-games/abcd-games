import {BattleshipShip} from "../../../types/BattleshipListDto.ts";
import {useDrag} from "react-dnd";

type Props = {
    ship: BattleshipShip;

}

export const shipLengths = {
    "CARRIER": 5,
    "BATTLESHIP": 4,
    "CRUISER": 3,
    "DESTROYER": 2
}
export default function BattleshipShipCard(props: Props) {


    const [{isDragging}, drag] = useDrag(() => ({
        type: "battleshipShip",
        item: {ship: props.ship},
        collect: monitor => ({
            isDragging: monitor.isDragging(),
        }),
    }), [props.ship])

    const css = isDragging ? "battleship_ship_selection border disabled" : "battleship_ship_selection border";
    return <div ref={drag} className={css}>
        {
            Array(shipLengths[props.ship])
                .fill(props.ship)
                .map((_, index) => <div key={index} className="battleship_ship_card border">{props.ship}</div>
        )}
    </div>;
}
