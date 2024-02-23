import {BattleshipShip} from "../../../types/Battleship.ts";
import BattleshipShipCard from "./BattleshipShipCard.tsx";

type Props = {
    availableShips: BattleshipShip[];
}



export default function BattleshipShipSelection(props: Readonly<Props>) {
    return (
        <div>
            {props.availableShips.map((ship, index) => (
                <BattleshipShipCard key={index} ship={ship}/>
            ))}
        </div>
    )
}
