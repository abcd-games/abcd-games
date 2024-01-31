import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {Stage} from "@pixi/react";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <Stage width={500} height={500} options={{backgroundColor: "gray"}}>
            <App/>
        </Stage>
    </React.StrictMode>,
)
