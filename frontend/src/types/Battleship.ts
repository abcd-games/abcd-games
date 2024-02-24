import {Player} from "./Player.ts";

export type Battleship = {
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

export type BattleshipConfig = {
    boardSize: number,
    requiredPlayers: number,
    maxPlayers: number,
    availableShipsPerPlayer: BattleshipShip[],
}

export type BattleshipTurnRequest = {
    x: number,
    y: number,
    targetPlayerId: string
}

export type BattleshipTurnResponse = {
    battleshipTurnRequest: BattleshipTurnRequest,
    result: BattleshipField,
}


export const defaultBattleshipConfig: BattleshipConfig = {
    boardSize: 10,
    requiredPlayers: 1,
    maxPlayers: 2,
    availableShipsPerPlayer: [
        "CARRIER",
        "BATTLESHIP",
        "BATTLESHIP",
        "CRUISER",
        "CRUISER",
        "CRUISER",
        "DESTROYER",
        "DESTROYER",
        "DESTROYER",
        "DESTROYER"]
}

export const defaultBattleshipBoard: BattleshipField[][] = [
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"],
    ["EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"]]

export const shipLengths: { CARRIER: number, BATTLESHIP: number, CRUISER: number, DESTROYER: number } = {
    "CARRIER": 5,
    "BATTLESHIP": 4,
    "CRUISER": 3,
    "DESTROYER": 2
}