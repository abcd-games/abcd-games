package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.*;
import com.github.abcdgames.backend.player.Player;
import com.github.abcdgames.backend.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BattleShipService {

    private final BattleshipRepository battleshipRepository;
    private final PlayerService playerService;

    public List<Battleship> findAll() {
        return battleshipRepository.findAll();
    }

    public Battleship createGame(CreateBattleshipRequest battleshipRequest, AppUser user) {
        Player player = playerService.getPlayerById(String.valueOf(user.getId()));

        Battleship createdBattleship = Battleship.builder()
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .maxPlayers(BattleshipConfig.defaultConfig.getMaxPlayers())
                .requiredPlayers(BattleshipConfig.defaultConfig.getRequiredPlayers())
                .playerBoards(Map.of(player, battleshipRequest.getBoard(), new Player("0", "BOT"), createRandomBoard()))
                .currentTurn(player)
                .build();

        return battleshipRepository.save(createdBattleship);
    }

    public Battleship findById(String id) {
        return battleshipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Battleship with id: " + id + " not found."));
    }

    public List<BattleshipTurnResponse> makeTurn(BattleshipTurnRequest battleshipTurnRequest, String id, AppUser user) {
        List<BattleshipTurnResponse> turnResponses = new ArrayList<>();

        Battleship battleship = findById(id);
        Player player = playerService.getPlayerById(String.valueOf(user.getId()));

        if (battleship.getCurrentTurn().getId().equals(player.getId())) {

            BattleshipField[][] board = battleship.getPlayerBoards().entrySet().stream()
                    .filter(playerEntry -> playerEntry.getKey().getId().equals(battleshipTurnRequest.targetPlayerId()))
                    .findAny()
                    .orElseThrow()
                    .getValue();

            BattleshipField target = board[battleshipTurnRequest.y()][battleshipTurnRequest.x()];

            if (target == BattleshipField.HIT || target == BattleshipField.MISS) {
                throw new IllegalArgumentException("Field already targeted.");
            }

            if (target == BattleshipField.SHIP) {
                board[battleshipTurnRequest.y()][battleshipTurnRequest.x()] = BattleshipField.HIT;
                turnResponses.add(BattleshipTurnResponse.builder()
                        .battleshipTurnRequest(battleshipTurnRequest)
                        .result(BattleshipField.HIT)
                        .build());
            } else {
                board[battleshipTurnRequest.y()][battleshipTurnRequest.x()] = BattleshipField.MISS;
                turnResponses.add(BattleshipTurnResponse.builder()
                        .battleshipTurnRequest(battleshipTurnRequest)
                        .result(BattleshipField.MISS)
                        .build());

                battleship.setCurrentTurn(battleship.getPlayerBoards().keySet().stream()
                        .filter(p -> !p.getId().equals(player.getId()))
                        .findAny()
                        .orElseThrow());

                while (battleship.getCurrentTurn().getId().equals("0")) {
                    turnResponses = generateTurn(battleship, turnResponses);
                }
            }

            battleshipRepository.save(battleship);

            return turnResponses;

        } else {
            throw new IllegalArgumentException("It's not your turn.");
        }
    }

    private static List<BattleshipTurnResponse> generateTurn(Battleship battleship, List<BattleshipTurnResponse> currentTurnResponses) {
        List<BattleshipTurnResponse> turnResponses = new ArrayList<>(currentTurnResponses);
        BattleshipField[][] board;
        BattleshipField target;
        Random random = new Random();

        int targetX = random.nextInt(10);
        int targetY = random.nextInt(10);

        Map.Entry<Player, BattleshipField[][]> targetEntry = battleship.getPlayerBoards().entrySet().stream()
                .filter(entry -> !entry.getKey().getId().equals(battleship.getCurrentTurn().getId()))
                .findAny()
                .orElseThrow();

        board = targetEntry.getValue();

        target = board[targetY][targetX];

        if (target == BattleshipField.SHIP) {
            board[targetY][targetX] = BattleshipField.HIT;
            turnResponses.add(BattleshipTurnResponse.builder()
                    .battleshipTurnRequest(BattleshipTurnRequest.builder()
                            .targetPlayerId(targetEntry.getKey().getId())
                            .x(targetX)
                            .y(targetY)
                            .build())
                    .result(BattleshipField.HIT)
                    .build());
        } else if (target == BattleshipField.EMPTY) {
            board[targetY][targetX] = BattleshipField.MISS;
            turnResponses.add(BattleshipTurnResponse.builder()
                    .battleshipTurnRequest(BattleshipTurnRequest.builder()
                            .targetPlayerId(targetEntry.getKey().getId())
                            .x(targetX)
                            .y(targetY)
                            .build())
                    .result(BattleshipField.MISS)
                    .build());
            battleship.setCurrentTurn(battleship.getPlayerBoards().keySet().stream()
                    .filter(p -> !p.getId().equals("0"))
                    .findAny()
                    .orElseThrow());
        }

        return turnResponses;
    }

    private BattleshipField[][] createRandomBoard() {
        BattleshipField[][] board = new BattleshipField[BattleshipConfig.defaultConfig.getBoardSize()][BattleshipConfig.defaultConfig.getBoardSize()];
        Random random = new Random();

        for (int i = 0; i < BattleshipConfig.defaultConfig.getBoardSize(); i++) {
            Arrays.fill(board[i], BattleshipField.EMPTY);
        }

        for (BattleshipShip battleshipShip : BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer()) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(BattleshipConfig.defaultConfig.getBoardSize());
                int y = random.nextInt(BattleshipConfig.defaultConfig.getBoardSize());
                boolean horizontal = random.nextBoolean();

                if (horizontal) {
                    if (x + battleshipShip.getLength() < BattleshipConfig.defaultConfig.getBoardSize()) {
                        boolean free = true;
                        for (int i = 0; i < battleshipShip.getLength(); i++) {
                            if (board[y][x + i] != BattleshipField.EMPTY) {
                                free = false;
                                break;
                            }
                        }
                        if (free) {
                            for (int i = 0; i < battleshipShip.getLength(); i++) {
                                board[y][x + i] = BattleshipField.SHIP;
                            }
                            placed = true;
                        }
                    }
                } else {
                    if (y + battleshipShip.getLength() < BattleshipConfig.defaultConfig.getBoardSize()) {
                        boolean free = true;
                        for (int i = 0; i < battleshipShip.getLength(); i++) {
                            if (board[y + i][x] != BattleshipField.EMPTY) {
                                free = false;
                                break;
                            }
                        }
                        if (free) {
                            for (int i = 0; i < battleshipShip.getLength(); i++) {
                                board[y + i][x] = BattleshipField.SHIP;
                            }
                            placed = true;
                        }
                    }
                }
            }
        }

        return board;
    }
}
