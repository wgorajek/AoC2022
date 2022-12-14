package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class Day13 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<PacketData> packets = getInput();
        var result = 0;
        for (var i = 0; i <= packets.size() - 2; i += 2) {
            if (packets.get(i).checkIfRightOrder(packets.get(i + 1))) {
                result += (i / 2) + 1;
            }
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        List<PacketData> packets = getInput();
        var packet = createPacket("[[2]]");
        packet.flag = 1;
        packets.add(packet);
        packet = createPacket("[[6]]");
        packet.flag = 2;
        packets.add(packet);

        Comparator<PacketData> comperator =
                (PacketData packet1, PacketData packet2) -> (packet1.checkIfRightOrder(packet2)) ? -1 : 1;
        packets.sort(comperator);
        int result = 1;
        for (int i = 0; i < packets.size(); i++) {
            if (packets.get(i).flag != 0) {
                result *= i+1;

            }
        }
        return result;
    }


    private static class PacketData {
        List<PacketData> childrens;
        PacketData root;
        PacketData next;
        int value;
        int flag = 0;

        public PacketData(PacketData root) {
            childrens = new ArrayList<PacketData>();
            this.root = root;
            if (root != null) {
                root.childrens.add(this);
            }
            next = null;
            value = -1;
        }

        public void setChildrenNext() {
            for (var i = 0; i <= childrens.size() - 2; i++) {
                childrens.get(i).next = childrens.get(i + 1);
            }
        }

        public Boolean checkIfRightOrder(PacketData rightPacket) {
            Boolean result = true;
            if (this.value != -1 && rightPacket.value != -1) {
                if (this.value < rightPacket.value) {
                    return true;
                } else if (this.value > rightPacket.value) {
                    return false;
                } else
                    return null;
            } else if (this.value == -1 && rightPacket.value == -1) {
                for (int i = 0; i <= Math.min(this.childrens.size(), rightPacket.childrens.size()) - 1; i++) {
                    result = childrens.get(i).checkIfRightOrder(rightPacket.childrens.get(i));
                    if (result != null) {
                        return result;
                    }
                }
                if (childrens.size() == rightPacket.childrens.size()) {
                    return null;
                } else {
                    return (childrens.size() < rightPacket.childrens.size());
                }
            } else {
                if (this.value != -1) {
                    var newChild = new PacketData(this);
                    newChild.value = this.value;
                    this.value = -1;
                }
                if (rightPacket.value != -1) {
                    var newChild = new PacketData(rightPacket);
                    newChild.value = rightPacket.value;
                    rightPacket.value = -1;
                }
                return this.checkIfRightOrder(rightPacket);
            }
        }
    }

    public PacketData createPacket(String line) {
        PacketData packet = new PacketData(null);
        PacketData actualPacket = packet;
        for (int i = 1; i < line.length() - 1; ) {
            if (line.charAt(i) == ',') {
                i++;
            } else if (line.charAt(i) == '[') {
                actualPacket = new PacketData(actualPacket);
                i++;
            } else if (line.charAt(i) == ']') {
                actualPacket = actualPacket.root;
                i++;
            } else {
                var newChild = new PacketData(actualPacket);
                var value = Integer.parseInt(String.valueOf(line.charAt(i)));
                i++;
                if (line.charAt(i) == '0') { //10
                    value = 10;
                    i++;
                }
                newChild.value = value;
            }

            packet.setChildrenNext();
        }
        return packet;
    }

    private List<PacketData> getInput() {
        final List<String> input = getInputLines();
        var packets = new ArrayList<PacketData>();
        for (int i = 0; i < input.size(); i += 3) {
            PacketData firstPacket = createPacket(input.get(i));
            PacketData secondPacket = createPacket(input.get(i + 1));
            packets.add(firstPacket);
            packets.add(secondPacket);
        }
        return packets;
    }
}
