import "pixi.js";
import {useState} from "react";
import BattleshipFieldView from "./BattleshipFieldView.tsx";


export type Battleship = BattleshipField[][]
export type BattleshipField = "EMPTY" | "SHIP" | "MISS" | "HIT"

const testBoard: Battleship = [["SHIP", "SHIP", "EMPTY"], ["EMPTY", "EMPTY", "EMPTY"], ["EMPTY", "SHIP", "EMPTY"]]

export default function App() {

    const [fields, setFields] = useState<Battleship>(testBoard);


    return (
        <>
            {
                fields.map((row, rowIndex, _) => {
                    return (
                        row.map((field, coloumnIndex, _) => {
                            return (
                                <BattleshipFieldView key={rowIndex + "-" + coloumnIndex}
                                                     field={field}
                                                     position={{x: coloumnIndex, y: rowIndex}}/>
                            )
                        })
                    )
                })
            }
        </>

    )
}
