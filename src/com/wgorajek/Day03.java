package com.wgorajek;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Day03 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<Rucksack> rucksacks = getInput();
        int sum = 0;
        for (var rucksack : rucksacks) {
            Set<Character> chars = rucksack.secondCompartment.chars()
                    .mapToObj(e->(char)e).collect(Collectors.toSet());
            for (Character letter : chars) {

                if (rucksack.firstCompartment.contains(letter.toString())) {
                    sum += getCharValue(letter);
                }
            }
        }
        return sum;
    }

    @Override
    public Object getPart2Solution() {
        final List<Rucksack> rucksacks = getInput();
        int sum = 0;
        for (var i = 0; i < rucksacks.size()/3; i++) {
            Set<Character> chars = rucksacks.get(3*i).allCompartments.chars()
                    .mapToObj(e->(char)e).collect(Collectors.toSet());
            for (Character letter : chars) {

                if (rucksacks.get(3*i+1).allCompartments.contains(letter.toString()) &
                    rucksacks.get(3*i+2).allCompartments.contains(letter.toString())) {
                    sum += getCharValue(letter);
                }
            }
        }

        return sum;
    }

    public int getCharValue(Character c) {
        var i = (int)c;
        if (i >= 97) {
            i = i - 96;
        }
        else {
            i = i - 38;
        }

        return i;
    }

    private static class Rucksack {
        public final String firstCompartment;
        public final String secondCompartment;
        public final String allCompartments;

        public Rucksack(String command) {
            firstCompartment = command.substring(0, (command.length()/2));
            secondCompartment = command.substring((command.length()/2));
            allCompartments = command;
        }

    }

    private List<Rucksack> getInput() {
        final List<String> input = getInputLines();
        final List<Rucksack> rucksacks = new ArrayList<Rucksack>();
        for (String i : input) {
            rucksacks.add(new Rucksack(i));
        }
        return rucksacks;
    }
}
