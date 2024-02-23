import {Route, Routes} from "react-router-dom";
import BattleshipGameCard from "../components/games/battleships/BattleshipGameCard.tsx";
import BattleshipListView from "../components/games/battleships/BattleshipListView.tsx";
import BattleshipCreateView from "../components/games/battleships/BattleshipCreateView.tsx";
import {AppUser} from "../types/AppUser.ts";


type Props = {
    appUser: AppUser | null

}
export default function BattleshipsPage(props: Readonly<Props>) {

    return (
        <>
            <h1>Battleships</h1>
            <Routes>
                <Route index element={<BattleshipListView/>}/>
                <Route path={"new"} element={<BattleshipCreateView/>}/>
                <Route path={":id"} element={<BattleshipGameCard appUser={props.appUser}/>}/>
            </Routes>
        </>

    )
}
