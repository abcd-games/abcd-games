import {Player} from "./Player.ts";

export type Battleship = {
    id: string,
    players: Player[],
    requiredPlayers: number,
    maxPlayers: number,
    boardPlayer1: BattleshipBoard,
    boardPlayer2: BattleshipBoard,
    availableShipsPerPlayer: BattleshipShip[],
    currentTurn: Player,
    winner: Player
}

export type BattleshipBoard = {
    id: string,
    fields: BattleshipField[][]
}

export type BattleshipField = "EMPTY" | "SHIP" | "HIT" | "MISS";


export type BattleshipShip = "CARRIER" | "BATTLESHIP" | "CRUISER" | "DESTROYER";
