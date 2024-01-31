import ship from "./139313fb368bb00.png";
import water from "./96f8f0cbf153ba33732c84b18a385cb4.jpg";
import {Sprite, useTick} from "@pixi/react";
import {useState} from "react";
import {BattleshipField} from "./App.tsx";

type BattleshipFieldProps = {
    position: {x: number, y:number}
    field: BattleshipField
}
export default function BattleshipFieldView(props: BattleshipFieldProps) {

    const [rotation, setRotation] = useState(0)
    const [animated, setAnimated] = useState(false)

    useTick(delta => {
        if (rotation >= 2*Math.PI) {
            setAnimated(false)
            setRotation(0)
            return
        }
        setRotation(prevState => prevState + .1*delta)
    }, animated)


    return (
        <Sprite
            onpointerdown={()=> setAnimated(true)}
            interactive={true}
            image={props.field === "SHIP" ? ship : water}
            scale={{x: .04, y: .04}}
            anchor={0.5}
            rotation={rotation}
            x={props.position.x * 50 + 50}
            y={props.position.y * 50 + 50}
        />
    )
}
