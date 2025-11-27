package com.tennis.scoring.domain.model;

public record Score(int pointsA, int pointsB, ScoreStatus status, Player advantage) {

    public static Score initial() {
        return new Score(0, 0, ScoreStatus.IN_PROGRESS, null);
    }

    public String display() {
        if (status == ScoreStatus.WON) {
            return advantage == Player.A ?
                    "Player A wins the game!" :
                    "Player B wins the game!";
        }

        if (status == ScoreStatus.DEUCE) {
            return "Deuce";
        }

        if (status == ScoreStatus.ADVANTAGE) {
            return advantage == Player.A ?
                    "Advantage Player A" :
                    "Advantage Player B";
        }

        return "Player A: %s / Player B: %s".formatted(
                toTennisScore(pointsA),
                toTennisScore(pointsB)
        );
    }

    private String toTennisScore(int points) {
        return switch (points) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            default -> "40";
        };
    }
}
