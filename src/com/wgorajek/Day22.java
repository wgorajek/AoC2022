package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day22 extends Solution {
    @Override
    public Object getPart1Solution() {
        var boardMap = getInput();
        var size = 50;

        for (var key : boardMap.map.keySet()) {
            var mapPoint = boardMap.map.get(key);
            mapPoint.findNeighbours(boardMap.map, size);
        }

        return boardMap.move(boardMap.map.get(new Point(50, 0)));
    }

    @Override
    public Object getPart2Solution() {
        var boardMap = getInput();
        var size = 50;

        for (var key : boardMap.map.keySet()) {
            var mapPoint = boardMap.map.get(key);
            if (mapPoint.y <= 49) {
                if (mapPoint.x <= 99) {
                    mapPoint.cubePart = 1;
                } else {
                    mapPoint.cubePart = 2;
                }
            } else if (mapPoint.y <= 99) {
                mapPoint.cubePart = 3;

            } else if (mapPoint.y <= 149) {
                if (mapPoint.x <= 49) {
                    mapPoint.cubePart = 4;
                } else {
                    mapPoint.cubePart = 5;
                }

            } else {
                mapPoint.cubePart = 6;
            }
        }

        for (var key : boardMap.map.keySet()) {
            var mapPoint = boardMap.map.get(key);
            mapPoint.findNeighboursCube(boardMap.map, size);
        }

        return boardMap.move(boardMap.map.get(new Point(50, 0)));
    }


    public class MapPoint {
        int x;
        int y;
        Point point;
        boolean isEmpty;
        MapPoint right;
        MapPoint left;
        MapPoint up;
        MapPoint down;
        int cubePart = 0;
        String direction = "";

        public MapPoint(int x, int y, boolean isEmpty) {
            this.x = x;
            this.y = y;
            this.isEmpty = isEmpty;
            point = new Point(x, y);
        }

        public void findNeighboursCube(HashMap<Point, MapPoint> map, int size) {
            if (left == null) {
                var leftPoint = new Point(x - 1, y);
                if (map.containsKey(leftPoint)) {
                    left = map.get(leftPoint);
                    left.right = this;

                } else {
                    if (this.cubePart == 1) {
                        leftPoint = new Point(0, 149 - y);
                        left = map.get(leftPoint);
                        left.left = this;
                    } else if (this.cubePart == 3) {
                        leftPoint = new Point(y - 50, 100);
                        left = map.get(leftPoint);
                        left.up = this;
                    } else if (this.cubePart == 6) {
                        leftPoint = new Point(y - 100, 0);
                        left = map.get(leftPoint);
                        left.up = this;
                    }
                }
            }
            if (right == null) {
                var rightPoint = new Point(x + 1, y);
                if (map.containsKey(rightPoint)) {
                    right = map.get(rightPoint);
                    right.left = this;
                } else {
                    if (this.cubePart == 2) {
                        rightPoint = new Point(99, 149 - y);
                        right = map.get(rightPoint);
                        right.right = this;
                    } else if (this.cubePart == 3) {
                        rightPoint = new Point(y + 50, 49);
                        right = map.get(rightPoint);
                        right.down = this;
                    } else if (this.cubePart == 6) {
                        rightPoint = new Point(y - 100, 149);
                        right = map.get(rightPoint);
                        right.down = this;
                    }
                }
            }
            if (up == null) {
                var upPoint = new Point(x, y - 1);
                if (map.containsKey(upPoint)) {
                    up = map.get(upPoint);
                    up.down = this;
                } else {
                    if (this.cubePart == 2) {
                        upPoint = new Point(x - 100, 199);
                        up = map.get(upPoint);
                        up.down = this;
                    }

                }
                if (down == null) {
                    var downPoint = new Point(x, y + 1);
                    if (map.containsKey(downPoint)) {
                        down = map.get(downPoint);
                        down.up = this;
                    }
                }
            }
        }

        public void findNeighbours(HashMap<Point, MapPoint> map, int size) {

            if (left == null) {
                var leftPoint = new Point(x - 1, y);
                if (map.containsKey(leftPoint)) {
                    left = map.get(leftPoint);
                } else {
                    var maxOpositePointSize = 1;
                    var opositePoint = this.point;
                    var newOpositePoint = opositePoint;
                    while (map.containsKey(newOpositePoint)) {
                        opositePoint = newOpositePoint;
                        newOpositePoint = new Point(x + size * maxOpositePointSize - 1, y);
                        maxOpositePointSize++;
                    }
                    left = map.get(opositePoint);
                }
                left.right = this;
            }
            if (right == null) {
                var rightPoint = new Point(x + 1, y);
                if (map.containsKey(rightPoint)) {
                    right = map.get(rightPoint);
                } else {
                    var maxOpositePointSize = 1;
                    var opositePoint = this.point;
                    var newOpositePoint = opositePoint;
                    while (map.containsKey(newOpositePoint)) {
                        opositePoint = newOpositePoint;
                        newOpositePoint = new Point(x - size * maxOpositePointSize + 1, y);
                        maxOpositePointSize++;
                    }
                    right = map.get(opositePoint);
                }
                right.left = this;
            }
            if (up == null) {
                var upPoint = new Point(x, y - 1);
                if (map.containsKey(upPoint)) {
                    up = map.get(upPoint);
                } else {
                    var maxOpositePointSize = 1;
                    var opositePoint = this.point;
                    ;
                    var newOpositePoint = opositePoint;
                    while (map.containsKey(newOpositePoint)) {
                        opositePoint = newOpositePoint;
                        newOpositePoint = new Point(x, y + size * maxOpositePointSize - 1);
                        maxOpositePointSize++;
                    }
                    up = map.get(opositePoint);
                }
                up.down = this;
            }
            if (down == null) {
                var downPoint = new Point(x, y + 1);
                if (map.containsKey(downPoint)) {
                    down = map.get(downPoint);
                } else {
                    var maxOpositePointSize = 1;
                    var opositePoint = this.point;
                    ;
                    var newOpositePoint = opositePoint;
                    while (map.containsKey(newOpositePoint)) {
                        opositePoint = newOpositePoint;
                        newOpositePoint = new Point(x, y - size * maxOpositePointSize + 1);
                        maxOpositePointSize++;
                    }
                    down = map.get(opositePoint);
                }
                down.up = this;
            }
        }

        public MapPoint moveAt(int length, String direction) {
            MapPoint newMapPoint = this;
            MapPoint oldMapPoint = this;
            for (var i = length; i > 0; i--) {
                if (direction.equals("left")) {
                    newMapPoint = oldMapPoint.left;
                } else if (direction.equals("right")) {
                    newMapPoint = oldMapPoint.right;
                } else if (direction.equals("up")) {
                    newMapPoint = oldMapPoint.up;
                } else if (direction.equals("down")) {
                    newMapPoint = oldMapPoint.down;
                }
                if (newMapPoint.isEmpty) {
                    oldMapPoint = newMapPoint;

                } else {
                    break;
                }

            }
            return oldMapPoint;
        }


        public MapPoint moveAtCube(int length, String direction) {
            MapPoint newMapPoint = this;
            MapPoint oldMapPoint = this;
            for (var i = length; i > 0; i--) {
                if (direction.equals("left")) {
                    newMapPoint = oldMapPoint.left;
                } else if (direction.equals("right")) {
                    newMapPoint = oldMapPoint.right;
                } else if (direction.equals("up")) {
                    newMapPoint = oldMapPoint.up;
                } else if (direction.equals("down")) {
                    newMapPoint = oldMapPoint.down;
                }
                if (newMapPoint.isEmpty) {
                    if (oldMapPoint.cubePart != newMapPoint.cubePart) {
                        var newDirection = transformDirection(oldMapPoint.cubePart, newMapPoint.cubePart);
                        if (!newDirection.isEmpty()) {
                            direction = newDirection;
                        }
                    }
                    oldMapPoint = newMapPoint;
                } else {
                    break;
                }

            }
            oldMapPoint.direction = direction;
            return oldMapPoint;
        }
    }

    private String transformDirection(int cubePartFrom, int cubePartTo) {
        if (cubePartFrom == 1) {
            if (cubePartTo == 6) {
                return "right";
            } else if (cubePartTo == 4) {
                return "right";
            }
        } else if (cubePartFrom == 2) {
            if (cubePartTo == 5) {
                return "left";
            } else if (cubePartTo == 3) {
                return "left";
            }
        } else if (cubePartFrom == 3) {
            if (cubePartTo == 4) {
                return "down";
            } else if (cubePartTo == 2) {
                return "up";
            }
        } else if (cubePartFrom == 4) {
            if (cubePartTo == 3) {
                return "right";
            } else if (cubePartTo == 1) {
                return "right";
            }
        } else if (cubePartFrom == 5) {
            if (cubePartTo == 6) {
                return "left";
            } else if (cubePartTo == 2) {
                return "left";
            }
        } else if (cubePartFrom == 6) {
            if (cubePartTo == 5) {
                return "up";
            } else if (cubePartTo == 1) {
                return "down";
            }
        }
        return "";
    }

    public class MapPointWithDirection {
        private final MapPoint mapPoint;
        private final String direction;

        public MapPointWithDirection(MapPoint mapPoint, String direction) {
            this.mapPoint = mapPoint;
            this.direction = direction;
        }
    }

    public class BoardMap {
        HashMap<Point, MapPoint> map;
        String moves;

        public BoardMap(HashMap<Point, MapPoint> map, String moves) {
            this.map = map;
            this.moves = moves;
        }

        public int move(MapPoint mapPoint) {
            var movesCount = 0;
            String direction = "right";
            Pattern pattern = Pattern.compile("(\\d+).*");


            while (movesCount < moves.length()) {
                if (moves.substring(movesCount, movesCount + 1).equals("L")) {
                    direction = turn("L", direction);
                    movesCount++;
                } else if (moves.substring(movesCount, movesCount + 1).equals("R")) {
                    direction = turn("R", direction);
                    movesCount++;
                } else {
                    Matcher m = pattern.matcher(moves.substring(movesCount));
                    m.matches();
                    mapPoint = mapPoint.moveAtCube(Integer.parseInt(m.group(1)), direction);
                    direction = mapPoint.direction;
                    movesCount += m.group(1).length();
                }

            }
            var directionValue = 0;
            if (direction.equals("up")) {
                directionValue = 3;
            } else if (direction.equals("down")) {
                directionValue = 1;
            } else if (direction.equals("left")) {
                directionValue = 2;
            }
            return (mapPoint.point.y + 1) * 1000 + (mapPoint.point.x + 1) * 4 + directionValue;
        }

        private String turn(String turnSide, String direction) {
            if (turnSide.equals("R")) {
                if (direction.equals("left")) {
                    return "up";
                } else if (direction.equals("up")) {
                    return "right";
                } else if (direction.equals("right")) {
                    return "down";
                } else if (direction.equals("down")) {
                    return "left";
                }
            } else {
                if (direction.equals("left")) {
                    return "down";
                } else if (direction.equals("up")) {
                    return "left";
                } else if (direction.equals("right")) {
                    return "up";
                } else if (direction.equals("down")) {
                    return "right";
                }
            }
            System.out.println("error");
            return "error";
        }
    }


    private BoardMap getInput() {
        final List<String> input = getInputLines();
        var map = new HashMap<Point, MapPoint>();

        for (var y = 0; y < input.size() - 2; y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                var str = line.substring(x, x + 1);
                if (str.equals(".")) {
                    var mapPoint = new MapPoint(x, y, true);
                    map.put(mapPoint.point, mapPoint);
                } else if (str.equals("#")) {
                    var mapPoint = new MapPoint(x, y, false);
                    map.put(mapPoint.point, mapPoint);
                }
            }
        }
        var moves = input.get(input.size() - 1);
        var boardMap = new BoardMap(map, moves);


        return boardMap;
    }
}
