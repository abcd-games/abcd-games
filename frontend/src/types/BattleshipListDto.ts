import {Player} from "./Player.ts";

export type BattleshipListDto = {
    id: string,
    players: Player[],
    requiredPlayers: number,
    maxPlayers: number,
    availableShipsPerPlayer: BattleshipShip[],
    currentTurn: Player,
    winner: Player
}

export type BattleshipDetails = {
    id: string,
    players: Player[],
    requiredPlayers: number,
    boards: {
        [key: string]: BattleshipField[][]
    },
    maxPlayers: number,
    availableShipsPerPlayer: BattleshipShip[],
    currentTurn: Player,
    winner: Player
}

export type BattleshipField = "EMPTY" | "SHIP" | "HIT" | "MISS";


export type BattleshipShip = "CARRIER" | "BATTLESHIP" | "CRUISER" | "DESTROYER";


export type  BattleshipCreation = {
    board: BattleshipField[][],
}
