package com.tennis.scoring.application.service;

import com.tennis.scoring.domain.exception.InvalidSequenceException;
import com.tennis.scoring.domain.model.Player;
import com.tennis.scoring.domain.service.TennisGameService;
import com.tennis.scoring.port.PlayGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TennisGameFacade implements PlayGame {
    Logger log = LoggerFactory.getLogger(TennisGameFacade.class);

    private final TennisGameService gameService;
    private final List<String> scoreHistory = new ArrayList<>();

    public TennisGameFacade(TennisGameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public List<String> playSequence(String sequence) {
        reset();

        if (sequence == null || sequence.isBlank()) {
            throw new InvalidSequenceException("Sequence cannot be null or empty");
        }
        log.info("Starting new game with sequence: {}", sequence);
        for (char c : sequence.toUpperCase().toCharArray()) {
            if (c != 'A' && c != 'B') {
                throw new InvalidSequenceException("Sequence must contain only 'A' or 'B'. Found: " + c);
            }
            Player winner = c == 'A' ? Player.A : Player.B;
            playerWinsBall(winner);
        }
        log.info("Game finished successfully with {} points played", scoreHistory.size());
        return List.copyOf(scoreHistory);
    }

    @Override
    public void playerWinsBall(Player player) {
        gameService.playerWinsBall(player);
        String current = gameService.getCurrentScore().display();
        scoreHistory.add(current);
    }

    @Override
    public void reset() {
        gameService.reset();
        scoreHistory.clear();
    }
}



