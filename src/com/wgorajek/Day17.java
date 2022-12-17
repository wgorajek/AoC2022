package com.wgorajek;


import com.jogamp.opengl.GLEventListenerState;

import javax.sound.sampled.Line;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 extends Solution {
    @Override
    public Object getPart1Solution() {
        var winds = getInput();
        return countHighestPoint(2022, winds);
    }

    public int countHighestPoint(int turns, String wind) {

        var winds = wind;
        Chamber chamber = new Chamber();
        int i = 0;
        for (int turn = 1; turn <= turns; turn++) {
            var shape = new Shape(Math.floorMod(turn - 1, 5), new Point(chamber.leftWall + 3, chamber.highestPoint - 4), chamber);
            for (var stillFalling = true; stillFalling; i = Math.floorMod(++i, winds.length())) {
                if (winds.charAt(i) == '<') {
                    shape.move(new Point(-1, 0));
                } else {
                    shape.move(new Point(1, 0));
                }
                stillFalling = shape.move(new Point(0, 1));
            }
        }
        return Math.abs(chamber.highestPoint);
    }

    @Override
    public Object getPart2Solution() {
        var winds = getInput();
        Chamber chamber = new Chamber();
        HashMap<Integer, HashSet<Point>> oldChambers = new HashMap<Integer, HashSet<Point>>();
        HashMap<Integer, Integer> windMapPos = new HashMap<Integer, Integer>();
        var repetitionTurn = 0;
        var turnsBeforeRepetition = 0;
        int i = 0;
        for (int turn = 1; turn <= 200000 && repetitionTurn == 0; turn++) {
            var shape = new Shape(Math.floorMod(turn - 1, 5), new Point(chamber.leftWall + 3, chamber.highestPoint - 4), chamber);
            for (var stillFalling = true; stillFalling; i = Math.floorMod(++i, winds.length())) {
                if (winds.charAt(i) == '<') {
                    shape.move(new Point(-1, 0));
                } else {
                    shape.move(new Point(1, 0));
                }
                stillFalling = shape.move(new Point(0, 1));
            }
            chamber.reshape();

            var newPoints = new HashSet<Point>();
            for (var point: chamber.points) {
                newPoints.add(new Point(point.x, point.y));
            }

            for (var key: oldChambers.keySet()) {
                var oldChamber = oldChambers.get(key);
                var validRepetition = true;
                for (var point: newPoints) {
                    if ( !oldChamber.contains(point)) {
                        validRepetition = false;
                        break;
                    }
                }
                for (var point: oldChamber) {
                    if ( !newPoints.contains(point)) {
                        validRepetition = false;
                        break;
                    }
                }
                if (validRepetition && (key%5 == turn %5) && (windMapPos.get(key) == i)) {
                    repetitionTurn = turn - key;
                    turnsBeforeRepetition = key;
                }
            }
            oldChambers.put(turn, newPoints);
            windMapPos.put(turn, i);
        }

        chamber = new Chamber();
        i = 0;
        var maxTurns = 1000000000000L;
        Long x1 = (long) countHighestPoint(turnsBeforeRepetition, winds);
        Long x2 = (long) countHighestPoint(turnsBeforeRepetition + repetitionTurn, winds);
        x2 = x2 - x1;
        Long x3 = (long) countHighestPoint(turnsBeforeRepetition + Math.floorMod(maxTurns-turnsBeforeRepetition, repetitionTurn), winds);
        Long x4 = Math.floorDiv(maxTurns-turnsBeforeRepetition, repetitionTurn) * x2 + x3;

        maxTurns = turnsBeforeRepetition + Math.floorMod(maxTurns - turnsBeforeRepetition, repetitionTurn);
        for (int turn = 1; turn <= maxTurns; turn++) {
            var shape = new Shape(Math.floorMod(turn - 1, 5), new Point(chamber.leftWall + 3, chamber.highestPoint - 4), chamber);
            for (var stillFalling = true; stillFalling; i = Math.floorMod(++i, winds.length())) {
                if (winds.charAt(i) == '<') {
                    shape.move(new Point(-1, 0));
                } else {
                    shape.move(new Point(1, 0));
                }
                stillFalling = shape.move(new Point(0, 1));
            }
        }
        return x4;
    }

    public class Chamber {
        HashSet<Point> points;
        int leftWall = -1;
        int rightWall = 7;
        int highestPoint = 0;
        int[] highestPoints = new int[7];

        public Chamber() {
            this.points = new HashSet<Point>();

            for (var i = leftWall + 1; i < rightWall; i++) {
                points.add(new Point(i, 0));
                highestPoints[i] = 0;
            }
        }

        public void addShape(Shape shape) {
            for (var point : shape.points) {
                highestPoint = Math.min(highestPoint, point.y);
                points.add(point);
            }
            for (var x = leftWall + 1; x < rightWall; x++) {
                var highestPointinThisColumn = 0;
                for (var point : shape.points) {
                    if (x == point.x) {
                        highestPointinThisColumn = Math.min(point.y, highestPointinThisColumn);
                    }
                }
                highestPoints[x] = Math.min(highestPoints[x], highestPointinThisColumn);
            }
        }

        public void reshape() {
            var highestPointInLowestColumn = highestPoint;
            for (var i = 0 ; i < 7; i ++) {
                highestPointInLowestColumn = Math.max(highestPoints[i], highestPointInLowestColumn);
            }

            if (highestPointInLowestColumn != 0) {
                var newPoints = new ArrayList<Point>();
                for (var point : points) {
                    if (point.y <= highestPointInLowestColumn) {
                        newPoints.add(new Point(point.x, point.y - highestPointInLowestColumn));
                    }
                }
                highestPoint = highestPoint - highestPointInLowestColumn;
                for (var i = 0 ; i < 7; i ++) {
                    highestPoints[i] = highestPoints[i] - highestPointInLowestColumn;
                }
                points.clear();
                points.addAll(newPoints);
            }

        }
    }

    public class Shape {
        private final Chamber chamber;
        HashSet<Point> points = new HashSet<Point>();

        public Shape(int kind, Point start, Chamber chamber) {
            this.chamber = chamber;
            if (kind == 0) {
                points.add(new Point(start.x, start.y));
                points.add(new Point(start.x + 1, start.y));
                points.add(new Point(start.x + 2, start.y));
                points.add(new Point(start.x + 3, start.y));
            } else if (kind == 1) {
                points.add(new Point(start.x + 1, start.y));
                points.add(new Point(start.x, start.y - 1));
                points.add(new Point(start.x + 1, start.y - 1));
                points.add(new Point(start.x + 2, start.y - 1));
                points.add(new Point(start.x + 1, start.y - 2));
            } else if (kind == 2) {
                points.add(new Point(start.x, start.y));
                points.add(new Point(start.x + 1, start.y));
                points.add(new Point(start.x + 2, start.y));
                points.add(new Point(start.x + 2, start.y - 1));
                points.add(new Point(start.x + 2, start.y - 2));
            } else if (kind == 3) {
                points.add(new Point(start.x, start.y));
                points.add(new Point(start.x, start.y - 1));
                points.add(new Point(start.x, start.y - 2));
                points.add(new Point(start.x, start.y - 3));
            } else if (kind == 4) {
                points.add(new Point(start.x, start.y));
                points.add(new Point(start.x + 1, start.y));
                points.add(new Point(start.x, start.y - 1));
                points.add(new Point(start.x + 1, start.y - 1));
            }
        }

        public boolean move(Point vector) {
            var newPoints = new HashSet<Point>();
            for (var point : points) {
                newPoints.add(new Point(point.x + vector.x, point.y + vector.y));
            }
            for (var point : newPoints) {
                if (vector.y == 1) {
                    if (chamber.points.contains(point)) {
                        chamber.addShape(this); //dont move, stop
                        return false;
                    }
                } else {
                    if (point.x <= chamber.leftWall || point.x >= chamber.rightWall || chamber.points.contains(point)) {
                        return true; //dont move , dont stop
                    }
                }
            }
            points.clear();
            points.addAll(newPoints);
            return true;
        }
    }

    private String getInput() {
        final List<String> input = getInputLines();
        return input.get(0);
    }
}
