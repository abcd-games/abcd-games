import {useEffect, useState} from "react";
import {BattleshipDetails, BattleshipTurnRequest, BattleshipTurnResponse} from "../../../types/Battleship.ts";
import BattleshipBoardCard from "./BattleshipBoardCard.tsx";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AppUser} from "../../../types/AppUser.ts";

type Props = {
    appUser: AppUser | null
}

export default function BattleshipGameCard(props: Readonly<Props>) {


    const [game, setGame] = useState<BattleshipDetails>();
    const [turn, setTurn] = useState<boolean>(false);

    const {id} = useParams<{ id: string }>();

    useEffect(() => {
        if (id === undefined) return;
        axios.get('/api/games/battleships/' + id)
            .then(response => {
                setGame(response.data)
                if (response.data.currentTurn.id === props.appUser?.id.toString()) {
                    setTurn(true)
                }
            })
    }, [id]);

    if (game === undefined) {
        return <div>Loading...</div>
    }

    const applyTurnResponse = (response: BattleshipTurnResponse) => {
        if (response === undefined) return;
        const board = game.boards[response.battleshipTurnRequest.targetPlayerId];
        board[response.battleshipTurnRequest.y][response.battleshipTurnRequest.x] = response.result;
        setGame(prevState => {
            if (prevState === undefined) return prevState;
            return {...prevState, boards: {...game.boards, [response.battleshipTurnRequest.targetPlayerId]: board}}
        })
    }

    const applyMultipleTurnsResponse = (responses: BattleshipTurnResponse[], startIndex: number) => {
        setTimeout(function () {
            applyTurnResponse(responses[startIndex])
            if (startIndex < responses.length) {
                applyMultipleTurnsResponse(responses, startIndex + 1)
            } else {
                setTurn(game.currentTurn.id === props.appUser?.id.toString())
            }
        }, 1000)
    }

    const makeTurn = (turnRequest: BattleshipTurnRequest) => {
        if (!turn) return;
        if (game.boards[turnRequest.targetPlayerId][turnRequest.y][turnRequest.x] !== "EMPTY") return;

        setTurn(false)
        axios.post('/api/games/battleships/' + id + '/turn', turnRequest)
            .then(response => {
                applyTurnResponse(response.data[0])
                applyMultipleTurnsResponse(response.data, 1)
            })
            .catch(() => setTurn(game.currentTurn.id === props.appUser?.id.toString()))
    }

    return (
        <div>
            <div className="battleship_board_own">
                <p>My Board</p>
                {props.appUser && <BattleshipBoardCard board={game.boards[props.appUser.id]}
                                                       setup={false}
                                                       onShipSelect={() => {
                                                       }}
                                                       onFieldClick={() => {
                                                       }}/>
                }
            </div>

            <div className={turn ? "battleship_board_enemy" : "battleship_board_enemy disabled"}>
                <p>Enemy Board</p>
                {Object.keys(game.boards).filter(key => key !== props.appUser?.id.toString()).map(key => (
                    <BattleshipBoardCard key={key}
                                         board={game.boards[key]}
                                         setup={false}
                                         onShipSelect={() => {
                                         }}
                                         onFieldClick={(position) => makeTurn({...position, targetPlayerId: key})}/>))}

            </div>
        </div>
    );
}
