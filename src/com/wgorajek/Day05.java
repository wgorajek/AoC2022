package com.wgorajek;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 extends Solution {
    @Override
    public Object getPart1Solution() {
        final CargoCrain cargoCrain = getInput();
        int j = 0;
        for (var command : cargoCrain.commands) {
            var stackFrom = cargoCrain.stacks.get(command.moveFrom-1);
            var stackTo = cargoCrain.stacks.get(command.moveTo-1);
            for (int i = 0; i < command.howMany ; i++) {
                stackTo.add(stackFrom.pop());
            }

        }

        String result = "";
        for (var stack: cargoCrain.stacks) {
            result += stack.pop();
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        final CargoCrain cargoCrain = getInput();
        int j = 0;
        var tmpStack = new Stack<String>();
        for (var command : cargoCrain.commands) {
            var stackFrom = cargoCrain.stacks.get(command.moveFrom-1);
            var stackTo = cargoCrain.stacks.get(command.moveTo-1);

            for (int i = 0; i < command.howMany ; i++) {
                tmpStack.add(stackFrom.pop());
            }
            for (int i = 0; i < command.howMany ; i++) {
                stackTo.add(tmpStack.pop());
            }

        }

        String result = "";
        for (var stack: cargoCrain.stacks) {
            result += stack.pop();
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

    private CargoCrain getInput() {
        final List<String> input = getInputLines();
        final List<Stack<String>> stacks = new ArrayList<Stack<String>>();
        List<Command> commands = new ArrayList<Command>();
        int stacksCount = 0;
        int j = 0;
        for (String i : input) {
            if (i.isEmpty()) {
                break;
            }
            j++;
        }
        int cargoHeight = j - 1;

        int numberOfStacks = Integer.parseInt(input.get(j-1).substring(input.get(j-1).length()-2, input.get(j-1).length()-1));
        for (int i = 0; i < numberOfStacks; i++) {
            stacks.add(new Stack<String>());
        }
        for (int height = cargoHeight-1; height >= 0; height--) {
            for (int i = 0; i < numberOfStacks; i++) {
                var line = input.get(height);
                var character = Character.toString(line.toCharArray()[1 + 4 * i]);
                if (!character.equals(" ")) {
                    stacks.get(i).add(character);
                }
            }
        }
        Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
        for (int i = cargoHeight+2; i <= input.size()-1; i++) {
            Matcher m = pattern.matcher(input.get(i));
            m.matches();
            commands.add(new Command(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3))));
        }
        CargoCrain cargoCrain = new CargoCrain(commands, stacks);
        return cargoCrain;
    }
}
