import {BattleshipListDto} from "../types/BattleshipListDto.ts";
import {useEffect, useState} from "react";
import axios from "axios";

export default function useBattleships() {
    const [battleships, setBattleships] = useState<BattleshipListDto[]>([]);

    useEffect(() => {
        fetchBattleships();
    }, []);

    function fetchBattleships() {
        axios.get('/api/games/battleships')
            .then(response => setBattleships(response.data))
    }

    function startBattleshipGame(battleship: BattleshipListDto) {
        axios.post('/api/games/battleships', battleship)
            .then(response => {
                const newBattleship = response.data;
                setBattleships([...battleships, newBattleship]);
            });
    }

    return {battleships, startBattleshipGame}
}
