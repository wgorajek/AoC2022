package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Day23 extends Solution {
    @Override
    public Object getPart1Solution() {
        var grovePoints = getInput();
        var moveVectors = getVectors();

        var newGrovePoints = new HashMap<Point, GrovePoint>();

        for (var i = 0; i < 10; i++) {
            newGrovePoints.clear();
            for (var point : grovePoints.keySet()) {
                var grovePoint = grovePoints.get(point);
                grovePoint.checkNegihbours(grovePoint, grovePoints, newGrovePoints, i, moveVectors);
            }
            var tmpGrovePoints = new HashMap<Point, GrovePoint>();
            for (var point : grovePoints.keySet()) {
                var grovePoint = grovePoints.get(point);
                if (newGrovePoints.get(grovePoint.moveToPoint).numberOfElfs == 1) {
                    tmpGrovePoints.put(grovePoint.moveToPoint, new GrovePoint(grovePoint.moveToPoint));
                } else {
                    tmpGrovePoints.put(grovePoint.point, new GrovePoint(grovePoint.point));
                }
            }
            grovePoints.clear();

            for (var point : tmpGrovePoints.keySet()) {
                grovePoints.put(point, tmpGrovePoints.get(point));
            }
        }
        int maxX = -100;
        int maxY = -100;
        int minX = 100;
        int minY = 100;
        for (var point : grovePoints.keySet()) {
            maxX = max(maxX, point.x);
            maxY = max(maxY, point.y);
            minX = min(minX, point.x);
            minY = min(minY, point.y);
        }
        return ((maxX - minX + 1) * (maxY - minY + 1)) - grovePoints.keySet().size();
    }

    @Override
    public Object getPart2Solution() {
        var grovePoints = getInput();
        var moveVectors = getVectors();

        var newGrovePoints = new HashMap<Point, GrovePoint>();
        var i = 0;
        for (; i < 100000; i++) {
            newGrovePoints.clear();
            for (var point : grovePoints.keySet()) {
                var grovePoint = grovePoints.get(point);
                grovePoint.checkNegihbours(grovePoint, grovePoints, newGrovePoints, i, moveVectors);
            }
            var tmpGrovePoints = new HashMap<Point, GrovePoint>();
            var isAnyElfMoving = false;
            for (var point : grovePoints.keySet()) {
                var grovePoint = grovePoints.get(point);
                if (newGrovePoints.get(grovePoint.moveToPoint).numberOfElfs == 1) {
                    tmpGrovePoints.put(grovePoint.moveToPoint, new GrovePoint(grovePoint.moveToPoint));
                    if (grovePoint.point != grovePoint.moveToPoint) {
                        isAnyElfMoving = true;
                    }
                } else {
                    tmpGrovePoints.put(grovePoint.point, new GrovePoint(grovePoint.point));
                }
            }
            grovePoints.clear();

            for (var point : tmpGrovePoints.keySet()) {
                grovePoints.put(point, tmpGrovePoints.get(point));
            }
            if (!isAnyElfMoving) {
                break;
            }
        }
        int maxX = -100;
        int maxY = -100;
        int minX = 100;
        int minY = 100;
        for (var point : grovePoints.keySet()) {
            maxX = max(maxX, point.x);
            maxY = max(maxY, point.y);
            minX = min(minX, point.x);
            minY = min(minY, point.y);
        }
        return i+1;

    }

    public HashMap<Integer, List<Point>> getVectors() {
        var moveVectors = new HashMap<Integer, List<Point>>();
        var vectors1 = new ArrayList<Point>();
        vectors1.add(new Point(-1, -1));
        vectors1.add(new Point(0, -1));
        vectors1.add(new Point(1, -1));
        var vectors2 = new ArrayList<Point>();
        vectors2.add(new Point(-1, 1));
        vectors2.add(new Point(0, 1));
        vectors2.add(new Point(1, 1));
        var vectors3 = new ArrayList<Point>();
        vectors3.add(new Point(-1, -1));
        vectors3.add(new Point(-1, 0));
        vectors3.add(new Point(-1, 1));
        var vectors4 = new ArrayList<Point>();
        vectors4.add(new Point(1, -1));
        vectors4.add(new Point(1, 0));
        vectors4.add(new Point(1, 1));
        moveVectors.put(0, vectors1);
        moveVectors.put(1, vectors2);
        moveVectors.put(2, vectors3);
        moveVectors.put(3, vectors4);
        return moveVectors;
    }

    public class GrovePoint {
        int numberOfElfs = 1;
        Point point;
        Point moveToPoint;

        public GrovePoint(Point point) {
            this.point = point;
        }

        public void checkNegihbours(GrovePoint grovePoint, HashMap<Point, GrovePoint> grovePoints, HashMap<Point, GrovePoint> newGrovePoints, int i, HashMap<Integer, List<Point>> moveVectors) {
            var elfMove = false;

            var isAnyElfPresent = false;
            for (var j = 0; j < 4; j++) {
                for (var vector : moveVectors.get((i + j) % 4)) {
                    var point = new Point(grovePoint.point.x + vector.x, grovePoint.point.y + vector.y);
                    isAnyElfPresent = isAnyElfPresent || grovePoints.containsKey(point);
                }
            }
            if (isAnyElfPresent) {

                for (var j = 0; j < 4; j++) {
                    var isElfPresent = false;
                    for (var vector : moveVectors.get((i + j) % 4)) {
                        var point = new Point(grovePoint.point.x + vector.x, grovePoint.point.y + vector.y);
                        isElfPresent = isElfPresent || grovePoints.containsKey(point);

                    }
                    if (!isElfPresent) {
                        var vector = moveVectors.get((i + j) % 4).get(1);
                        var point = new Point(vector.x + grovePoint.point.x, vector.y + grovePoint.point.y);
                        grovePoint.moveToPoint = point;
                        if (newGrovePoints.containsKey(point)) {
                            newGrovePoints.get(point).numberOfElfs++;
                        } else {
                            newGrovePoints.put(point, new GrovePoint(point));
                        }
                        elfMove = true;
                        break;
                    }
                }
                if (!elfMove) {
                    newGrovePoints.put(grovePoint.point, new GrovePoint(grovePoint.point));
                    grovePoint.moveToPoint = grovePoint.point;
                }
            } else {
                newGrovePoints.put(grovePoint.point, new GrovePoint(grovePoint.point));
                grovePoint.moveToPoint = grovePoint.point;
            }
        }
    }


    private HashMap<Point, GrovePoint> getInput() {
        final List<String> input = getInputLines();
        var grovePoints = new HashMap<Point, GrovePoint>();


        for (var y = 0; y < input.size(); y++) {
            for (var x = 0; x < input.get(0).length(); x++) {
                var point = new Point(x, y);
                if (input.get(y).charAt(x) == '#') {
                    grovePoints.put(point, new GrovePoint(point));
                }
            }
        }

        return grovePoints;
    }
}
