import {Player} from "./Player.ts";

export type Battleship = {
    id: string,
    players: Player[],
    requiredPlayers: number,
    maxPlayers: number,
    boardPlayer1: BattleshipField[][],
    boardPlayer2: BattleshipField[][],
    availableShipsPerPlayer: BattleshipShip[],
    currentTurn: Player,
    winner: Player
}

export type BattleshipField = "EMPTY" | "SHIP" | "HIT" | "MISS";


export type BattleshipShip = "CARRIER" | "BATTLESHIP" | "CRUISER" | "DESTROYER";


export type  BattleshipCreation = Omit<Battleship, "id" | "players" | "currentTurn" | "winner" | "requiredPlayers" | "maxPlayers" | "boardPlayer2">;
