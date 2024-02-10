import {Route, Routes} from "react-router-dom";
import BattleshipGameCard from "../components/games/battleships/BattleshipGameCard.tsx";
import BattleshipListView from "../components/games/battleships/BattleshipListView.tsx";
import BattleshipCreateView from "../components/games/battleships/BattleshipCreateView.tsx";


export default function BattleshipsPage() {

    return (
        <>
            <h1>Battleships</h1>
            <Routes>
                <Route index element={<BattleshipListView/>}/>
                <Route path={"new"} element={<BattleshipCreateView/>}/>
                <Route path={":id"} element={<BattleshipGameCard/>}/>
            </Routes>
        </>

    )
}