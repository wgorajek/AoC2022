package com.wgorajek;


import com.jogamp.opengl.GLEventListenerState;

import javax.sound.sampled.Line;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class Day18 extends Solution {
    @Override
    public Object getPart1Solution() {
        var cubes = getInput();
        for (var i = 0; i < cubes.size() - 1; i++) {
            var cube = cubes.get(i);
            for (var j = i + 1; j < cubes.size(); j++) {
                cube.checkConnected(cubes.get(j));
            }
        }
        int result = 0;
        for (var cube : cubes) {
            result += cube.surface;
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        var cubes = getInput();
        for (var i = 0; i < cubes.size() - 1; i++) {
            var cube = cubes.get(i);
            for (var j = i + 1; j < cubes.size(); j++) {
                cube.checkConnected(cubes.get(j));
            }
        }
        int result = 0;
        for (var cube : cubes) {
            result += cube.surface;
        }

        HashSet<Point3d> cubes3d = new HashSet<Point3d>();
        HashMap<Point3d, Cube> airCubesMap = new HashMap<Point3d, Cube>();
        List<Cube> airCubes = new ArrayList<Cube>();
        int maxx = 0;
        int minx = 1000;
        int maxy = 0;
        int miny = 1000;
        int maxz = 0;
        int minz = 1000;
        for (var cube : cubes) {
            cubes3d.add(new Point3d(cube.x, cube.y, cube.z));
            maxx = Integer.max(maxx, cube.x);
            minx = Integer.min(minx, cube.x);
            maxy = Integer.max(maxy, cube.y);
            miny = Integer.min(miny, cube.y);
            maxz = Integer.max(maxz, cube.z);
            minz = Integer.min(minz, cube.z);
        }


        for (var x = minx; x <= maxx; x++) {
            for (var y = miny; y <= maxy; y++) {
                for (var z = minz; z <= maxz; z++) {
                    var point3d = new Point3d(x, y, z);
                    if (!cubes3d.contains(point3d)) {
                        var cube = new Cube(x, y, z);
                        airCubes.add(cube);
                        airCubesMap.put(new Point3d(x, y, z), cube);
                    }
                }
            }
        }

        //find all airCubes that are trapped
        for (var airCube : airCubes) {
            if (airCube.x == maxx || airCube.x == minx || airCube.y == maxy || airCube.y == miny || airCube.x == maxz || airCube.x == minz) {
                airCube.trapped = false;
            }
        }

        var trappedAirCubes = new ArrayList<Cube>();


        for (var j = 0; j < 10; j++) {
            for (var airCube : airCubes) {
                airCube.isNeigbourFree(airCubesMap);
            }
        }

        for (var airCube : airCubes) {
            if (airCube.trapped) {
                trappedAirCubes.add(airCube);
            }
        }


        for (var i = 0; i < trappedAirCubes.size() - 1; i++) {
            var cube = trappedAirCubes.get(i);
            for (var j = i + 1; j < trappedAirCubes.size(); j++) {
                cube.checkConnected(trappedAirCubes.get(j));
            }
        }
        var trappedSpace = 0;
        for (var cube : trappedAirCubes) {
            trappedSpace += cube.surface;
        }

        return result-trappedSpace;
    }

    private static class Point3d {
        int x;
        int y;
        int z;

        public Point3d(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Point3d add(Point3d p1, Point3d p2) {
            return new Point3d(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
        }

        public boolean equals(Object obj) {
            return ((Point3d) obj).x == x && ((Point3d) obj).y == y && ((Point3d) obj).z == z;
        }

        @Override
        public int hashCode() {
            return x + 100 * y + 10000 * z;
        }
    }

    public class Cube {
        HashSet<Point> points;
        int x;
        int y;
        int z;
        int surface = 6;
        boolean trapped = true;

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void checkConnected(Cube cube) {
            if ((x == cube.x && (y == cube.y) && abs(z - cube.z) == 1) || (x == cube.x && (z == cube.z) && abs(y - cube.y) == 1) || (z == cube.z && (y == cube.y) && abs(x - cube.x) == 1)) {
                surface--;
                cube.surface--;

            }

        }


        public boolean isNeigbourFree(HashMap<Point3d, Cube> cubes) {
            var neighbours = new ArrayList<Point3d>();
            neighbours.add(new Point3d(x - 1, y, z));
            neighbours.add(new Point3d(x + 1, y, z));
            neighbours.add(new Point3d(x - 1, y, z));
            neighbours.add(new Point3d(x, y + 1, z));
            neighbours.add(new Point3d(x, y - 1, z));
            neighbours.add(new Point3d(x, y, z + 1));
            neighbours.add(new Point3d(x, y, z - 1));
            for (var neighbour : neighbours) {
                var neighbourCube = cubes.get(neighbour);
                if (neighbourCube != null && !neighbourCube.trapped) {
                    this.trapped = false;
                    break;
                }
            }
            return !trapped;
        }
    }


    private List<Cube> getInput() {
        final List<String> input = getInputLines();
        var cubes = new ArrayList<Cube>();

        for (var line : input) {
            var coordinates = line.split(",");
            cubes.add(new Cube(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2])));
        }

        return cubes;
    }
}
