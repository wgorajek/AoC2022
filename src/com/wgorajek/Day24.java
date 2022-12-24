package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Day24 extends Solution {
    @Override
    public Object getPart1Solution() {
        var valley = getInput();

        var myPositions = new HashSet<Point>();
        var newMyPositions = new HashSet<Point>();
        myPositions.add(new Point(valley.entryPoint.x, valley.entryPoint.y));

        while(true) {
            valley.pointsWithBlizzard.clear();
            for (var blizzard: valley.blizzards) {
                blizzard.move(valley);
                valley.pointsWithBlizzard.add(blizzard.location);
            }
            newMyPositions.clear();
            for (var position: myPositions) {
                newMyPositions.addAll(getNeighboursWithoutBlizzard(valley, position));
            }
            myPositions.clear();
            myPositions.addAll(newMyPositions);
            valley.time++;
            if (myPositions.contains(valley.preExitPoint)) {
                break;
            }
        }
        return valley.time+1;
    }

    @Override
    public Object getPart2Solution() {
        var valley = getInput();
        var startingEntryPoint = valley.entryPoint;
        var startingExitPoint = valley.preExitPoint;

        var myPositions = new HashSet<Point>();
        var newMyPositions = new HashSet<Point>();
        myPositions.add(new Point(valley.entryPoint.x, valley.entryPoint.y));

        while(true) {
            valley.moveBlizzard();
            newMyPositions.clear();
            for (var position: myPositions) {
                newMyPositions.addAll(getNeighboursWithoutBlizzard(valley, position));
            }
            myPositions.clear();
            myPositions.addAll(newMyPositions);
            valley.time++;
            if (myPositions.contains(valley.preExitPoint)) {
                break;
            }
        }
        valley.moveBlizzard();
        valley.time++;
//***
        valley.entryPoint = new Point(startingExitPoint.x, startingExitPoint.y + 1);
        valley.preExitPoint = new Point (startingEntryPoint.x, startingEntryPoint.y + 1);
        myPositions.clear();
        myPositions.add(new Point(valley.entryPoint.x, valley.entryPoint.y));

        while(true) {
            valley.moveBlizzard();
            newMyPositions.clear();
            for (var position: myPositions) {
                newMyPositions.addAll(getNeighboursWithoutBlizzard(valley, position));
            }
            myPositions.clear();
            myPositions.addAll(newMyPositions);
            valley.time++;
            if (myPositions.contains(valley.preExitPoint)) {
                break;
            }
        }
        valley.moveBlizzard();
        valley.time++;
//***
        valley.entryPoint = startingEntryPoint;
        valley.preExitPoint = startingExitPoint;
        myPositions.clear();
        myPositions.add(new Point(valley.entryPoint.x, valley.entryPoint.y));

        while(true) {
            valley.moveBlizzard();
            newMyPositions.clear();
            for (var position: myPositions) {
                newMyPositions.addAll(getNeighboursWithoutBlizzard(valley, position));
            }
            myPositions.clear();
            myPositions.addAll(newMyPositions);
            valley.time++;
            if (myPositions.contains(valley.preExitPoint)) {
                break;
            }
        }


        return valley.time+1;
    }

    public List<Point> getNeighboursWithoutBlizzard(Valley valley, Point myPosition) {
        var result = new ArrayList<Point>();
        if (!valley.pointsWithBlizzard.contains(myPosition)) {
            result.add(myPosition);
        }

        var vectors = new ArrayList<Point>();
        vectors.add(new Point(0,1));
        vectors.add(new Point(0,-1));
        vectors.add(new Point(-1,0));
        vectors.add(new Point(1,0));

        for (var vector: vectors){
            var point = new Point(vector.x + myPosition.x, vector.y + myPosition.y);
            if (point.x <= valley.maxX && point.x >= valley.minX && point.y <= valley.maxY && point.y >= valley.minY && !valley.pointsWithBlizzard.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }


    public class Blizzard {
        Point location;
        Point direction;

        public Blizzard(Point location, char directionVector) {
            this.location = location;
            if (directionVector == '<') {
                direction = new Point(-1, 0);
            } else if (directionVector == '>') {
                direction = new Point(1, 0);
            } else if (directionVector == '^') {
                direction = new Point(0, -1);
            } else if (directionVector == 'v') {
                direction = new Point(0, 1);
            }
        }

        public void move(Valley valley) {
            location = new Point((location.x + direction.x + valley.maxX+1) % (valley.maxX+1), (location.y + direction.y + valley.maxY+1) % (valley.maxY+1));
        }
    }

    public class Valley {
        int time = 0;
        int minX;
        int maxX;
        int minY;
        int maxY;
        Point entryPoint;
        Point preExitPoint;
        HashSet<Blizzard> blizzards;
        HashSet<Point> pointsWithBlizzard;

        public Valley(int minX, int maxX, int minY, int maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            blizzards = new HashSet<Blizzard>();
            pointsWithBlizzard = new HashSet<Point>();
        }

        public void moveBlizzard() {
            pointsWithBlizzard.clear();
            for (var blizzard: blizzards) {
                blizzard.move(this);
                pointsWithBlizzard.add(blizzard.location);
            }
        }
    }

    private Valley getInput() {
        final List<String> input = getInputLines();
        var valley = new Valley(0, input.get(0).length() - 3, 0, input.size() - 3);

        for (var x = valley.minX; x <= valley.maxX; x++) {
            for (var y = valley.minY; y <= valley.maxY; y++) {
                var valleyPoint = input.get(y+1).charAt(x+1);
                if (valleyPoint != '.') {
                    var point = new Point(x, y);
                    valley.pointsWithBlizzard.add(point);
                    valley.blizzards.add(new Blizzard(point, valleyPoint));
                }
            }
        }

        for (var x = valley.minX; x <= valley.maxX; x++) {

        }
        valley.entryPoint = new Point(input.get(0).indexOf('.')-1, -1);
        valley.preExitPoint = new Point(input.get(valley.maxY+2).indexOf('.')-1, valley.maxY);

        return valley;
    }
}
