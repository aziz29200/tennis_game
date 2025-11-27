package com.tennis.scoring.domain.service;

import com.tennis.scoring.domain.model.Player;
import com.tennis.scoring.domain.model.ScoreStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TennisGameServiceTest {

    private final TennisGameService gameService = new TennisGameService();

    @BeforeEach
    void setUp() {
        gameService.reset();
    }

    @Test
    void should_increment_scores_correctly() {
        gameService.playerWinsBall(Player.A);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player A: 15 / Player B: 0");

        gameService.playerWinsBall(Player.A);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player A: 30 / Player B: 0");

        gameService.playerWinsBall(Player.A);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player A: 40 / Player B: 0");
    }

    @Test
    void player_A_should_win_directly() {
        gameService.playerWinsBall(Player.A);
        gameService.playerWinsBall(Player.A);
        gameService.playerWinsBall(Player.A);
        gameService.playerWinsBall(Player.A);

        assertThat(gameService.getCurrentScore().status()).isEqualTo(ScoreStatus.WON);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player A wins the game!");
    }

    @Test
    void should_handle_deuce_advantage_and_win() {
        // To 40-40
        for (int i = 0; i < 3; i++) {
            gameService.playerWinsBall(Player.A);
            gameService.playerWinsBall(Player.B);
        }
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Deuce");

        // Advantage A
        gameService.playerWinsBall(Player.A);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Advantage Player A");

        // Back to Deuce
        gameService.playerWinsBall(Player.B);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Deuce");

        // Advantage B
        gameService.playerWinsBall(Player.B);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Advantage Player B");

        // Win B
        gameService.playerWinsBall(Player.B);
        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player B wins the game!");
    }

    @Test
    void should_throw_exception_if_play_after_win() {
        for (int i = 0; i < 4; i++) {
            gameService.playerWinsBall(Player.A);
        }
        assertThatThrownBy(() -> gameService.playerWinsBall(Player.A))
                .isInstanceOf(com.tennis.scoring.domain.exception.GameFinishedException.class)
                .hasMessage("Game is already finished");
    }

    @Test
    void should_handle_extended_deuce() {
        // 40-40
        for (int i = 0; i < 3; i++) {
            gameService.playerWinsBall(Player.A);
            gameService.playerWinsBall(Player.B);
        }
        // Deuce

        gameService.playerWinsBall(Player.A); // Adv A
        gameService.playerWinsBall(Player.B); // Deuce
        gameService.playerWinsBall(Player.A); // Adv A
        gameService.playerWinsBall(Player.A); // Win A

        assertThat(gameService.getCurrentScore().display()).isEqualTo("Player A wins the game!");
    }
}
