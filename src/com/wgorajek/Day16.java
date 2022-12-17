package com.wgorajek;


import com.jogamp.opengl.GLEventListenerState;

import javax.sound.sampled.Line;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<Room> sensors = getInput();
        int result = 0;
        return result;
    }

    @Override
    public Object getPart2Solution() {
//        List<Sensor> sensors = getInput();
        int result = 0;
        return result;
    }


    public class Room {
        int flowRate;
        String name;
        boolean isOpen = false;
        List<Room> neighbours;
        HashMap<Room, Integer> neighboursWithDistance;

        public Room(int flowRate, String name) {
            this.flowRate = flowRate;
            this.name = name;
            neighboursWithDistance = new HashMap<Room, Integer>();
            neighbours = new ArrayList<Room>();

        }
    }

    private List<Room> getInput() {
        //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        final List<String> input = getInputLines();
        List<Room> rooms = new ArrayList<Room>();
        HashMap<String, Room> roomsDictionary = new HashMap<String, Room>();
        for (var line : input) {
            Pattern pattern = Pattern.compile("Valve (\\S+) has flow rate=(\\d+); tunnels? leads? to valves?(.+)");
            Matcher m = pattern.matcher(line);
            m.matches();
            var room = new Room(Integer.parseInt(m.group(2)), m.group(1));
            rooms.add(room);
            roomsDictionary.put(m.group(1), room);
        }
        for (var line : input) {
            Pattern pattern = Pattern.compile("Valve (\\S+) has flow rate=(\\d+); tunnels? leads? to valves?(.+)");
            Matcher m = pattern.matcher(line);
            m.matches();
            var room = roomsDictionary.get(m.group(1));
            for (var name: m.group(3).split(",")) {
                room.neighbours.add(roomsDictionary.get(name.trim()));
            }
        }
        

        return rooms;
    }
}
