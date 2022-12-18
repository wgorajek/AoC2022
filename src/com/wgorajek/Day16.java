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

        int result = 0;
        List<Room> rooms = getInput();
        Room roomAA = null;
        for (var room : rooms) {
            if (room.name.equals("AA")) {
                roomAA = room;
            }
        }

        var startingRooms = findStartingRoomsWithFlow(rooms, roomAA, 30);

        var roomsWithFlow = new HashSet<Room>();
        for (var startingRoom: startingRooms) {
            roomsWithFlow.clear();
            roomsWithFlow.addAll(startingRoom.room.neighboursWithDistance.keySet());
            result = Math.max(recursiveFlow(startingRoom.room, roomsWithFlow, startingRoom.timeLeft), result);
        }

        return result;
    }

    @Override
    public Object getPart2Solution() {
        List<Room> rooms = getInput();
        var roomDict = new HashMap<String, Room>();
        Room roomAA = null;
        for (var room : rooms) {
            if (room.name.equals("AA")) {
                roomAA = room;
            }
            roomDict.put(room.name, room);
        }

        var startingRooms = findStartingRoomsWithFlow(rooms, roomAA, 26);

        HashSet<Room> myRooms = new HashSet<Room>();
        HashSet<Room> elephantRooms = new HashSet<Room>();
        var roomsWithFlow2 = new ArrayList<Room>();
        for (var room : rooms) {
            if (room.flowRate > 0) {
                roomsWithFlow2.add(room);
            }
        }

        HashSet<String> myRoomsStr = new HashSet<String>();
        myRoomsStr = getAllMyRooms(roomsWithFlow2);

        int max = 0;
        while (myRoomsStr.iterator().hasNext()) {
            var myRoomStr = myRoomsStr.iterator().next();
            myRoomsStr.remove(myRoomStr);
            myRooms.clear();
            elephantRooms.clear();
            for (int i = 0; i < myRoomStr.length(); i += 2) {
                var roomName = myRoomStr.substring(i, i + 2);
                myRooms.add(roomDict.get(roomName));
            }
            for (var room : roomDict.values()) {
                if (!myRooms.contains(room)) {
                    if (room.flowRate > 0) {
                        elephantRooms.add(room);
                    }
                }
            }

            var roomsWithFlow = new HashSet<Room>();
            int result = 0;
            for (var startingRoom : startingRooms) {
                if (elephantRooms.contains(startingRoom.room)) {
                    roomsWithFlow.clear();
                    roomsWithFlow.addAll(elephantRooms);
                    roomsWithFlow.remove(startingRoom.room);
                    result = Math.max(recursiveFlow(startingRoom.room, roomsWithFlow, startingRoom.timeLeft), result);
                }
            }
            var result2 = 0;
            for (var startingRoom : startingRooms) {
                if (myRooms.contains(startingRoom.room)) {
                    roomsWithFlow.clear();
                    roomsWithFlow.addAll(myRooms);
                    roomsWithFlow.remove(startingRoom.room);
                    result2 = Math.max(recursiveFlow(startingRoom.room, roomsWithFlow, startingRoom.timeLeft), result2);
                }
            }
            max = Math.max(result + result2, max);
        }
        return max;
    }

    private HashSet<String> getAllMyRooms(List<Room> rooms) {
        var result = new HashSet<String>();
        for (var i = 1; i < Math.floor(rooms.size() / 2); i++) {
            var tmp = getAllSubsets(rooms, i);
            tmp = sortStr(tmp);
            result.addAll(tmp);
        }

        return result;
    }

    private HashSet<String> sortStr(HashSet<String> names) {
        Comparator<String> c = Comparator.comparing((String x) -> x);
        var sorted = new HashSet<String>();
        for (var name : names) {
            var nameList = new ArrayList<String>();
            for (int i = 0; i < name.length(); i += 2) {
                var roomName = name.substring(i, i + 2);
                nameList.add(roomName);
            }
            nameList.sort(c);
            var newName = "";
            for (var nameShort : nameList) {
                newName = newName + nameShort;
            }
            sorted.add(newName);
        }

        return sorted;
    }

    public HashSet<String> getAllSubsets(List<Room> rooms, int number) {
        var result = new HashSet<String>();
        if (number > 0) {
            for (var j = 0; j < rooms.size(); j++) {
                var name = rooms.get(j).name;
                var newRooms = new ArrayList<Room>();
                newRooms.addAll(rooms);
                newRooms.remove(j);
                var subsets = getAllSubsets(newRooms, number - 1);
                for (var subset: subsets) {
                    result.add(name + subset);
                }
            }
        } else {
            result.add("");
            return result;
        }
        return result;
    }


    public int recursiveFlow(Room actualRoom, HashSet<Room> unvisitedRooms, int time) {
        if (time <= 1) {
            return 0;
        }
        time--;
        var result = actualRoom.flowRate * time;
        int bestRoom = 0;
        for (var room : unvisitedRooms) {
            var newUnvisitedRooms = new HashSet<Room>();
            newUnvisitedRooms.addAll(unvisitedRooms);
            newUnvisitedRooms.remove(room);
            bestRoom = Math.max(bestRoom, recursiveFlow(room, newUnvisitedRooms, time - (actualRoom.neighboursWithDistance.get(room))));
        }

        return result + bestRoom;
    }

    private List<StartingRoom> findStartingRoomsWithFlow(List<Room> rooms, Room startingRoom, int startingtime) {
        var result = new ArrayList<StartingRoom>();
        var roomsWithoutFlow = new LinkedList<Room>();
        roomsWithoutFlow.add(startingRoom);
        var visitedRooms = new HashSet<Room>();
        var time = startingtime;

        var newRoomsWithoutFlow = new LinkedList<Room>();
        while (!roomsWithoutFlow.isEmpty() || time > 1) {
            while (!roomsWithoutFlow.isEmpty()) {
                var room = roomsWithoutFlow.pop();
                if (!visitedRooms.contains(room)) {
                    if (room.flowRate == 0) {
                        visitedRooms.add(room);
                        for (var neighbour : room.neighbours) {
                            if (!visitedRooms.contains(neighbour)) {
                                newRoomsWithoutFlow.addAll(room.neighbours);
                            }
                        }
                    } else {
                        result.add(new StartingRoom(room, time));
                        visitedRooms.add(room);
                    }
                }
            }
            roomsWithoutFlow.addAll(newRoomsWithoutFlow);
            newRoomsWithoutFlow.clear();
            time--;
        }


        return result;
    }

    private class StartingRoom {
        Room room;
        int timeLeft;

        public StartingRoom(Room room, int timeLeft) {
            this.room = room;
            this.timeLeft = timeLeft;
        }
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

        public void findNeigboursWithFlow(int numberOfRoomsWithFlow) {
            int roomsFound = 1; //self
            var roomsReached = new ArrayList<Room>();
            roomsReached.add(this);
            var roomsToCheck = new LinkedList<Room>();
            int distance = 1;
            roomsToCheck.addAll(this.neighbours);
            while (roomsFound < numberOfRoomsWithFlow) {
                var newRoomsTocheck = new LinkedList<Room>();
                while (!roomsToCheck.isEmpty()) {
                    var room = roomsToCheck.pop();
                    if (!roomsReached.contains(room)) {
                        if (room.flowRate > 0) {
                            neighboursWithDistance.put(room, distance);
                            roomsFound++;
                        }
                        roomsReached.add(room);
                        newRoomsTocheck.addAll(room.neighbours);
                    }
                }
                distance++;
                roomsToCheck.addAll(newRoomsTocheck);
            }

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
            for (var name : m.group(3).split(",")) {
                room.neighbours.add(roomsDictionary.get(name.trim()));
            }
        }

        List<Room> roomsWithFlow = new ArrayList<Room>();
        for (var room : rooms) {
            if (room.flowRate > 0) {
                roomsWithFlow.add(room);
            }
        }
        for (var roomFrom : roomsWithFlow) {
            roomFrom.findNeigboursWithFlow(roomsWithFlow.size());
        }

        return rooms;
    }
}
