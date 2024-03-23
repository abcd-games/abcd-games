import {BattleshipShip} from "../../../types/Battleship.ts";
import BattleshipShipCard from "./BattleshipShipCard.tsx";

type Props = {
    availableShips: BattleshipShip[];
}



export default function BattleshipShipSelection(props: Readonly<Props>) {
    return (
        <div className="battleship_ship_selection">
            <div>
                {props.availableShips.map((ship, index) => (
                    <BattleshipShipCard alignment="horizontal" key={"horizontal" + index} ship={ship}/>
                ))}
            </div>
           <div className="battleship_ship_selection_ship_horizontal">
               {props.availableShips.map((ship, index) => (
                   <BattleshipShipCard alignment="vertical" key={"horizontal" + index} ship={ship}/>
               ))}
           </div>
        </div>
    )
}
