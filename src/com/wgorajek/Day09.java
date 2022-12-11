package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;

public class Day09 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<Command> commands = getInput();
        Point head = new Point(0, 0);
        Point tail = new Point(0, 0);
        Set<Point> pointsVisited = new HashSet<Point>();
        for (Command command : commands) {
            for (int i = 1; i <= command.length; i++) {
                head = new Point(head.x + command.direction.x, head.y + command.direction.y);
                if (maxDistance(head, tail) > 1) {
                    tail = diagonalMove(tail, head);
                }
                pointsVisited.add(new Point(tail.x, tail.y));
            }
        }
        return pointsVisited.size();
    }

    @Override
    public Object getPart2Solution() {
        List<Command> commands = getInput();
        Point[] knots = new Point[10];
        for (int j = 0; j <= 9; j++) {
            knots[j] = new Point(0, 0);
        }
        Set<Point> pointsVisited = new HashSet<Point>();
        for (Command command : commands) {
            for (int i = 1; i <= command.length; i++) {
                knots[0] = new Point(knots[0].x + command.direction.x, knots[0].y + command.direction.y);
                for (int j = 1; j <= 9; j++) {
                    if (maxDistance(knots[j - 1], knots[j]) > 1) {
                        knots[j] = diagonalMove(knots[j], knots[j - 1]);
                    }
                }
                pointsVisited.add(new Point(knots[9].x, knots[9].y));
            }
        }
        return pointsVisited.size();
    }

    public int maxDistance(Point p1, Point p2) {
        return Integer.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));

    }

    public Point diagonalMove(Point p1, Point p2) {
        int xDiffrence = p2.x - p1.x;
        int yDiffrence = p2.y - p1.y;
        if (xDiffrence != 0) {
            xDiffrence = xDiffrence / Math.abs(xDiffrence);
        }
        if (yDiffrence != 0) {
            yDiffrence = yDiffrence / Math.abs(yDiffrence);
        }
        return new Point(p1.x + xDiffrence, p1.y + yDiffrence);
    }

    private static class Command {
        Point direction;
        int length;

        public Command(Point direction, int length) {
            this.direction = direction;
            this.length = length;
        }
    }


    private List<Command> getInput() {
        final List<String> input = getInputLines();
        List<Command> commands = new ArrayList<Command>();
        for (String line : input) {
            Point direction;
            if (line.substring(0, 1).equals("R")) {
                direction = new Point(1, 0);
            } else if (line.substring(0, 1).equals("L")) {
                direction = new Point(-1, 0);
            } else if (line.substring(0, 1).equals("U")) {
                direction = new Point(0, 1);
            } else {
                direction = new Point(0, -1);
            }
            int length = Integer.parseInt(line.substring(2));
            commands.add(new Command(direction, length));

        }
        return commands;
    }
}
