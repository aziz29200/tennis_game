package com.tennis.scoring.domain.service;

import com.tennis.scoring.domain.exception.GameFinishedException;
import com.tennis.scoring.domain.model.Player;
import com.tennis.scoring.domain.model.Score;
import com.tennis.scoring.domain.model.ScoreStatus;


public class TennisGameService {

    private Score currentScore = Score.initial();

    public void playerWinsBall(Player player) {
        if (currentScore.status() == ScoreStatus.WON) {
            throw new GameFinishedException("Game is already finished");
        }
        currentScore = computeNextScore(currentScore, player);
    }

    private Score computeNextScore(Score score, Player winner) {
        int pointsA = score.pointsA();
        int pointsB = score.pointsB();

        if (score.status() == ScoreStatus.ADVANTAGE) {
            if (winner == score.advantage()) {
                return new Score(pointsA, pointsB, ScoreStatus.WON, winner);
            } else {
                return new Score(pointsA, pointsB, ScoreStatus.DEUCE, null);
            }
        }
        if (score.status() == ScoreStatus.DEUCE) {
            return new Score(pointsA, pointsB, ScoreStatus.ADVANTAGE, winner);
        }

        // Normal case
        if (winner == Player.A) {
            pointsA++;
        } else {
            pointsB++;
        }

        if (pointsA >= 4 && pointsA - pointsB >= 2) {
            return new Score(pointsA, pointsB, ScoreStatus.WON, Player.A);
        }
        if (pointsB >= 4 && pointsB - pointsA >= 2) {
            return new Score(pointsA, pointsB, ScoreStatus.WON, Player.B);
        }
        if (pointsA >= 3 && pointsB >= 3) {
            return new Score(3, 3, ScoreStatus.DEUCE, null);
        }
        return new Score(pointsA, pointsB, ScoreStatus.IN_PROGRESS, null);
    }

    public Score getCurrentScore() {
        return currentScore;
    }

    public void reset() {
        currentScore = Score.initial();
    }
}
