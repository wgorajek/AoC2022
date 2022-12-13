package com.wgorajek;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Day12 extends Solution {

    @Override
    public Object getPart1Solution() {
        var cave = getInput();
        return cave.findBestPath().length;
    }

    @Override
    public Object getPart2Solution() {
        var cave = getInput();
        var startingPoints = cave.getLowestPoints();
        var result = 9999999;
        startingPoints.size();
        for (var point : startingPoints) {
            cave.startPoint = point;
            var bestPath = cave.findBestPath();
            if (bestPath != null) {
                result = Integer.min(bestPath.length, result);
            }
        }
        return result;
    }

    private class CavePoint {
        Point point;
        Integer level;
        List<CavePoint> neighbours;

        public CavePoint(Point point, Integer level) {
            this.point = point;
            this.level = level;
            neighbours = new ArrayList<CavePoint>();
        }
    }

    private class Path {
        Integer length;
        CavePoint lastPoint;

        public Path(Integer length, CavePoint lastPoint) {
            this.length = length;
            this.lastPoint = lastPoint;
        }

        public void addNewPoint(CavePoint point) {
            lastPoint = point;
            length++;
        }
    }

    private class Cave {
        CavePoint[][] cave;
        CavePoint finishPoint;
        CavePoint startPoint;

        public Cave(Integer[][] caveMap) {
            cave = new CavePoint[caveMap.length][caveMap[0].length];
            for (var i = 0; i < caveMap.length; i++) {
                for (var j = 0; j < caveMap[0].length; j++) {
                    cave[i][j] = new CavePoint(new Point(i, j), caveMap[i][j]);
                }
            }
            int[] vector = {-1, 0, 1};
            for (var i = 0; i < cave.length; i++) {
                for (var j = 0; j < cave[i].length; j++) {
                    if (cave[i][j].level.equals(27)) {
                        finishPoint = cave[i][j];
                    } else if (cave[i][j].level.equals(0)) {
                        startPoint = cave[i][j];
                    }
                    for (var vi : vector) {
                        for (var vj : vector) {
                            var x = i + vi;
                            var y = j + vj;
                            if ((vi == 0 ^ vj == 0) && validPoint(x, y) && (cave[x][y].level - cave[i][j].level) <= 1) {
                                cave[i][j].neighbours.add(cave[x][y]);
                            }
                        }
                    }
                }
            }
        }

        public List<CavePoint> getLowestPoints() {
            var points = new ArrayList<CavePoint>();
            for (var i = 0; i < cave.length; i++) {
                for (var j = 0; j < cave[i].length; j++) {
                    if (cave[i][j].level.equals(1)) {
                        points.add(cave[i][j]);
                    }
                }
            }
            return points;
        }

        private boolean validPoint(int x, int y) {
            return (x >= 0 && y >= 0 && x < cave.length && y < cave[0].length);
        }

        public Path findBestPath() {
            var bestPathMap = new HashMap<CavePoint, Path>();
            Integer bestPathLength = 9999999;
            var pathStack = new Stack<Path>();
            var firstPath = new Path(0, startPoint);
            pathStack.push(firstPath);
            bestPathMap.put(firstPath.lastPoint, firstPath);
            while (!pathStack.isEmpty()) {
                var path = pathStack.pop();
                for (var neighbour : path.lastPoint.neighbours) {
                    var newPath = new Path(path.length, path.lastPoint);
                    newPath.addNewPoint(neighbour);
                    var bestToThisPoint = bestPathMap.get(neighbour);
                    if (bestToThisPoint == null || bestToThisPoint.length > newPath.length) {
                        bestPathMap.put(newPath.lastPoint, newPath);
                        if (newPath.lastPoint != finishPoint) {
                            if (newPath.length < bestPathLength) {
                                pathStack.push(newPath);
                            }
                        } else {
                            bestPathLength = Integer.min(bestPathLength, newPath.length);
                        }
                    }
                }
            }

            return bestPathMap.get(finishPoint);
        }


    }

    private Cave getInput() {
        var input = getInputLines();
        var caveMap = new Integer[input.size()][input.get(0).length()];
        for (var i = 0; i < input.size(); i++) {
            var line = input.get(i);
            for (var j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'S') {
                    caveMap[i][j] = 0;//E
                } else if (line.charAt(j) == 'E') {
                    caveMap[i][j] = 27;//E
                } else {

                    caveMap[i][j] = Integer.valueOf(line.substring(j, j + 1).toCharArray()[0]) - Integer.valueOf("a".toCharArray()[0]) + 1;
                }
            }
        }
        return new Cave(caveMap);
    }

    private Cave getInputLarge() {
        var input = getInputLines();
        var sizeX = input.size();
        var sizeY = input.get(0).length();
        var caveMap = new Integer[input.size() * 5][input.get(0).length() * 5];

        for (var k = 0; k < 5; k++) {
            for (var l = 0; l < 5; l++) {
                for (var i = 0; i < input.size(); i++) {
                    var line = input.get(i);
                    for (var j = 0; j < line.length(); j++) {
                        var value = Integer.parseInt(line.substring(j, j + 1));
                        value += k + l;
                        if (value > 9) {
                            value = value - 9;
                        }
//2835
                        caveMap[sizeX * k + i][sizeY * l + j] = value;
                    }
                }
            }
        }
        return new Cave(caveMap);
    }
}
