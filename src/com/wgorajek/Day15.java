package com.wgorajek;


import javax.sound.sampled.Line;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<Sensor> sensors = getInput();
        var beacons = new HashSet<Point>();
        int result = 0;
        int checkedLine = 2000000;

        var lines = new ArrayList<Line>();
        for (var sensor : sensors) {
            var line = createLineAtY(sensor.x, sensor.y, checkedLine, Math.abs(sensor.x - sensor.beaconX) + Math.abs(sensor.y - sensor.beaconY));
            if (line != null) {
                lines.add(line);
                var newBeacon = new Point(sensor.beaconX, sensor.beaconY);
                if (sensor.beaconY == checkedLine && !beacons.contains(newBeacon)) {
                    result--;
                }
                beacons.add(newBeacon);
            }
        }
        Queue<Line> lineQueue = new LinkedList<Line>();
        Queue<Line> newQueue = new LinkedList<Line>();
        for (var line : lines) {
            newQueue.clear();
            while (!lineQueue.isEmpty()) {
                newQueue.addAll(mergeLines(line, lineQueue.remove()));
            }
            newQueue.add(line);
            lineQueue.addAll(newQueue);
        }
        for (var line : lineQueue) {
            result += line.x2 - line.x1 + 1;
        }

        return result;
    }

    @Override
    public Object getPart2Solution() {
        List<Sensor> sensors = getInput();
        Comparator<Line> comperator = (Line line1, Line line2) -> (line1.x1 - line2.x1);
        int result = 0;
//        int checkedLine = 2000000;
//        int checkedLine = 2000000;
//        for (var i = 0; i <= 20; i++) {
        for (var i = 0; i <= 4000000; i++) {
            var lines = new ArrayList<Line>();
            for (var sensor : sensors) {
                var line = createLineAtY(sensor.x, sensor.y, i, Math.abs(sensor.x - sensor.beaconX) + Math.abs(sensor.y - sensor.beaconY));
                if (line != null) {
                    lines.add(line);
                    var newBeacon = new Point(sensor.beaconX, sensor.beaconY);
//                    if (sensor.beaconY == checkedLine && !beacons.contains(newBeacon)) {
//                        result--;
//                    }
//                    beacons.add(newBeacon);
                }
            }
            Queue<Line> lineQueue = new LinkedList<Line>();
            Queue<Line> newQueue = new LinkedList<Line>();
            for (var line : lines) {
                newQueue.clear();
                while (!lineQueue.isEmpty()) {
                    newQueue.addAll(mergeLines(line, lineQueue.remove()));
                }
                newQueue.add(line);
                lineQueue.addAll(newQueue);
            }
            var checkedLines = new ArrayList<Line>();
            checkedLines.addAll(lineQueue);
            checkedLines.sort(comperator);
            var maxX = 0;
            var minX = 0;
            for (var line: checkedLines) {
                if (maxX + 1 < line.x1) {
                    System.out.println("found x = " + (maxX+1) + " y = " + i);
                }
                maxX = Math.max(line.x2, maxX);
            }
        }
        return result;
    }




    public Line createLineAtY(int x, int y, int lineY, int radius) {
        var length = radius - Math.abs(lineY - y);
        if (length >= 0) {
            return new Line(x - length, x + length);
        }
        return null;
    }

    public ArrayList<Line> mergeLines(Line line1, Line line2) {
        var result = new ArrayList<Line>();
        if (line1.x1 >= line2.x1 && line1.x2 <= line2.x2) { //line1 inside line2
            var lineTmp = new Line(line2.x1, line1.x1 - 1);
            if (lineTmp.isValid()) {
                result.add(lineTmp);
            }
            lineTmp = new Line(line1.x2 + 1, line2.x2);
            if (lineTmp.isValid()) {
                result.add(lineTmp);
            }

        } else if (line2.x1 >= line1.x1 && line2.x2 <= line1.x2) { //line2 inside line1

        } else if (line2.x1 > line1.x2 || line2.x2 < line1.x1) { //lines dont cross
            result.add(line2);
        } else if (line1.x1 >= line2.x1) {
            line2.x2 = line1.x1 - 1;
            if (line2.isValid()) {
                result.add(line2);
            }
        } else if (line1.x1 <= line2.x1) {
            line2.x1 = line1.x2 + 1;
            if (line2.isValid()) {
                result.add(line2);
            }
        }
        return result;
    }


    private class Line {
        int x1;
        int x2;

        public Line(int x1, int x2) {
            this.x1 = x1;
            this.x2 = x2;
        }

        public boolean isValid() {
            return x1 <= x2;
        }
    }

    public class Sensor {
        int x;
        int y;
        int beaconX;
        int beaconY;

        public Sensor(int x, int y, int beaconX, int beaconY) {
            this.x = x;
            this.y = y;
            this.beaconX = beaconX;
            this.beaconY = beaconY;
        }
    }

    private List<Sensor> getInput() {
        final List<String> input = getInputLines();
        List<Sensor> sensors = new ArrayList<Sensor>();
        for (var line : input) {
            Pattern pattern = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
            Matcher m = pattern.matcher(line);
            m.matches();
            sensors.add(new Sensor(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
        }

        return sensors;
    }
}
