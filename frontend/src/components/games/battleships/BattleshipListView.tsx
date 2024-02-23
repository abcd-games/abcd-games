import useBattleships from "../../../hooks/useBattleship.tsx";
import {Link, useNavigate} from "react-router-dom";

export default function BattleshipListView() {

    const {battleships} = useBattleships();
    const navigate = useNavigate();

    function startBattleshipGameClick() {
        navigate("new")
    }

    return (
        <div>
            {battleships.map(battleship => <Link to={battleship.id} key={battleship.id}>{battleship.id}</Link>)}
            <button onClick={startBattleshipGameClick} className="btn btn-outline-light">Start</button>
        </div>
    )
}
