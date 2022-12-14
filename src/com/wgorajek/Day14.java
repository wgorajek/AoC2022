package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class Day14 extends Solution {
    @Override
    public Object getPart1Solution() {
        HashSet<Point> points = getInput();
        int result = 0;
        int maxDepth = 0;
        for (var point: points) {
            maxDepth = Math.max(maxDepth, point.y);
        }
        while (dropSand(points, maxDepth)) {
            result++;
        }
        return result;
    }

    public boolean dropSand(HashSet<Point> points, int maxDepth) {
        var x = 500;
        var y = 0;
        while (true) {
            if (y > maxDepth) {
                return false;
            }
            var point1 = new Point(x, y + 1);
            var point2 = new Point(x-1, y + 1);
            var point3 = new Point(x+1, y + 1);
            if (!points.contains(point1)) {
                y++;
            } else if (!points.contains(point2)) {
                y++;
                x--;
            } else if (!points.contains(point3)) {
                y++;
                x++;
            } else {
                points.add(new Point(x, y));
                return true;
            }
        }
    }

    @Override
    public Object getPart2Solution() {
        HashSet<Point> points = getInput();
        int result = 0;
        int maxDepth = 0;
        for (var point: points) {
            maxDepth = Math.max(maxDepth, point.y);
        }
        maxDepth = maxDepth + 2;
        var startingPoint = new Point(500, 0);
        for (int i = 500 - maxDepth - 5; i <= 500 + maxDepth + 5; i++) {
            points.add(new Point(i, maxDepth));
        }
        while (dropSand(points, maxDepth+1)) {
            result++;
            if (points.contains(startingPoint)) {
                break;
            }
        }
        return result;
    }


    private HashSet<Point> getInput() {
        final List<String> input = getInputLines();
        HashSet<Point> points = new HashSet<Point>();
        for (var line : input) {
            var rockPoints = new ArrayList<Point>();
            var strPoints = line.split("->");
            for (var strPoint : strPoints) {
                rockPoints.add(new Point(Integer.parseInt(strPoint.split(",")[0].trim()), Integer.parseInt(strPoint.split(",")[1].trim())));
            }
            for (int i = 0; i < rockPoints.size() - 1; i++) {
                var point1 = rockPoints.get(i);
                var point2 = rockPoints.get(i + 1);

                if (point1.x == point2.x) {
                    var diffrenceYSign = point2.y - point1.y;
                    if (diffrenceYSign != 0) {
                        diffrenceYSign = diffrenceYSign / Math.abs(diffrenceYSign);
                    }
                    for (int j = 0; j <= Math.abs(point2.y - point1.y); j++) {
                        points.add(new Point(point1.x, point1.y + j * diffrenceYSign));
                    }
                }
                if (point1.y == point2.y) {
                    var diffrenceXSign = point2.x - point1.x;
                    if (diffrenceXSign != 0) {
                        diffrenceXSign = diffrenceXSign / Math.abs(diffrenceXSign);
                    }
                    for (int j = 0; j <= Math.abs(point2.x - point1.x); j++) {
                        points.add(new Point(point1.x + j * diffrenceXSign, point1.y));
                    }
                }
            }
        }
        return points;
    }
}
