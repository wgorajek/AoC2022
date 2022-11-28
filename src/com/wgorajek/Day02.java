package com.wgorajek;

import java.util.ArrayList;
import java.util.List;


public class Day02 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<Strategy> strategies = getInput();
        int totalScore = 0;
        for (var strategy : strategies) {
            totalScore += strategy.countScore();
        }
        return totalScore;
    }

    @Override
    public Object getPart2Solution() {
        final List<Strategy> strategies = getInput();
        int totalScore = 0;
        for (var strategy : strategies) {
            totalScore += strategy.countScoreB();
        }
        return totalScore;
    }


    private static class Strategy {
        public final String opponent;
        public final String response;

        public Strategy(String command) {
            final String[] parts = command.split("[ ]");
            this.opponent = parts[0];
            this.response = parts[1];
        }

        public int countScore() {
            int score = 0;
            int opponentValue;
            int responseValue;

            if (opponent.equals("A")) {
                opponentValue = 1;
            }
            else if (opponent.equals("B")) {
                opponentValue = 2;
            }
            else {
                opponentValue = 3;
            }

            if (response.equals("X")) {
                responseValue = 1;
            }
            else if (response.equals("Y")) {
                responseValue = 2;
            }
            else {
                responseValue = 3;
            }

            if ((3 + opponentValue - responseValue) % 3 == 0) {
                score += 3; //draw
            }
            else if ((3 + opponentValue - responseValue) % 3 == 2)
            {
                score += 6; //won
            }
            score += responseValue;
            return score;
        }

        public int countScoreB() {
            int score = 0;
            int opponentValue;
            int responseValue;

            if (opponent.equals("A")) {
                opponentValue = 1;
            }
            else if (opponent.equals("B")) {
                opponentValue = 2;
            }
            else {
                opponentValue = 3;
            }

            if (response.equals("X")) {
                responseValue = ((3 + opponentValue - 1 - 1) % 3) + 1;
                score += 0 + responseValue;//lost
            }
            else if (response.equals("Y")) {
                score += 3 + opponentValue; //draw
            }
            else {
                responseValue = ((opponentValue - 1 + 1) % 3) + 1;
                score += 6 + responseValue;//won
            }
            return score;
        }


    }

    private List<Strategy> getInput() {
        final List<String> input = getInputLines();
        final List<Strategy> strategies = new ArrayList<Strategy>();
        for (String i : input) {
            strategies.add(new Strategy(i));
        }
        return strategies;
    }
}
