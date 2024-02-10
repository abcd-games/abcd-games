import useBattleships from "../../../hooks/useBattleship.tsx";
import {useNavigate} from "react-router-dom";

export default function BattleshipListView() {

    const {battleships} = useBattleships();
    const navigate = useNavigate();

    function startBattleshipGameClick() {
        navigate("new")
    }

    return (
        <div>
            {battleships.map(battleship => <div key={battleship.id} onClick={() => navigate(battleship.id)}>{battleship.id}</div>)}
            <button onClick={startBattleshipGameClick} className="btn btn-outline-light">Start</button>
        </div>
    )
}