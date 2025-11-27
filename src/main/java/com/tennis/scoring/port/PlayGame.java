package com.tennis.scoring.port;

import com.tennis.scoring.domain.model.Player;

import java.util.List;

public interface PlayGame {

    List<String> playSequence(String sequence);
    void playerWinsBall(Player player);
    void reset();
}
