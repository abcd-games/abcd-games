package com.github.abcdgames.backend.player;

import com.github.abcdgames.backend.player.exception.NoSuchPlayerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player getPlayerById(String id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new NoSuchPlayerException("Player with id: " + id + " does not exist."));
    }
}
