package com.wgorajek;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 extends Solution {
    @Override
    public Object getPart1Solution() {
        String input = getInput();
        var inputArray = input.toCharArray();
        int result = 0;
        for (int i=1; i <= inputArray.length-4; i++) {
            boolean found = true;
            for (int j = 0; j <= 2; j++ ){
                for (int k = j+1; k <= 3; k++ ){
                    if (inputArray[j + i] == inputArray[k + i]) {
                        found = false;
                    }
                }
            }
            if (found) {
                result = i + 4;
                break;
            }
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        String input = getInput();
        var inputArray = input.toCharArray();
        int result = 0;
        for (int i=1; i <= inputArray.length-14; i++) {
            boolean found = true;
            for (int j = 0; j <= 12; j++ ){
                for (int k = j+1; k <= 13; k++ ){
                    if (inputArray[j + i] == inputArray[k + i]) {
                        found = false;
                    }
                }
            }
            if (found) {
                result = i + 14;
                break;
            }
        }
        return result;
    }


    private static class Command {
        public final int moveFrom;
        public final int moveTo;
        public final int howMany;

        public Command(int howMany, int moveFrom, int moveTo) {
            this.howMany = howMany;
            this.moveFrom = moveFrom;
            this.moveTo = moveTo;
        }
    }

    private static class CargoCrain {
        List<Command> commands;
        List<Stack<String>> stacks;
        public CargoCrain(List<Command> commands, List<Stack<String>> stacks) {
            this.stacks = stacks;
            this.commands = commands;
        }
    }

    private String getInput() {
        final List<String> input = getInputLines();
        return input.get(0);
    }
}
