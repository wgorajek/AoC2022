package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;

public class Day10 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<Command> commands = getInput();
        CPU cpu = new CPU();
        int maxCycle = 220;
        int nextCycle = 20;
        int x = 1;
        int result = 0;
        for (var command : commands) {
            x = cpu.x;
            if (command.command.equals("noop")) {
                cpu.noop();
            } else {
                cpu.addx(command.value);
            }
            if (cpu.tick >= nextCycle - 1) {
                if (cpu.tick == nextCycle - 1) {
                    x = cpu.x;
                }

                result += x * nextCycle;
                nextCycle += 40;
            }
            if (nextCycle > maxCycle) {
                break;
            }
        }

        return result;
    }

    @Override
    public Object getPart2Solution() {
        List<Command> commands = getInput();
        CPU cpu = new CPU();
        int maxCycle = 220;
        int nextCycle = 20;
        int x = 1;
        int result = 0;

        for (var command : commands) {
            x = cpu.x;
            if (command.command.equals("noop")) {
                cpu.noop();
            } else {
                cpu.addx(command.value);
            }
            if (cpu.tick >= nextCycle - 1) {
                if (cpu.tick == nextCycle - 1) {
                    x = cpu.x;
                }

                result += x * nextCycle;
                nextCycle += 40;
            }
        }

        for (int i = 1; i <=6 ; i++) {
            System.out.println(cpu.output.substring(40*(i - 1),40*i));
        }

        return result;
    }

    private static class CPU {
        int tick;
        int x;
        String output = "";

        public CPU() {
            x = 1;
        }

        public void noop() {
            if (Math.abs(tick%40 - x) <= 1) {
                output += "#";
            } else {
                output += ".";
            }
            tick++;
        }

        public void addx(int value) {
            if (Math.abs(tick%40 - x) <= 1) {
                output += "#";
            } else {
                output += ".";
            }
            tick++;
            if (Math.abs(tick%40 - x) <= 1) {
                output += "#";
            } else {
                output += ".";
            }
            tick++;
            x += value;
        }
    }

    private static class Command {
        int value;
        String command;

        public Command(String command, int value) {
            this.command = command;
            this.value = value;
        }
    }

    private List<Command> getInput() {
        final List<String> input = getInputLines();
        final List<Command> commands = new ArrayList<Command>();
        for (var line : input) {
            if (line.contains(" ")) {
                var splitedLine = line.split(" ");
                commands.add(new Command(splitedLine[0], Integer.parseInt(splitedLine[1])));
            } else {
                commands.add(new Command("noop", 0));
            }

        }
        return commands;
    }
}
