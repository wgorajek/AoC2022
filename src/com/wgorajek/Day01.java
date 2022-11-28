package com.wgorajek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

    public class Day01 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<Elf> elves = getInput();
        int maxCalories = 0;
        for(var elf : elves ) {
            maxCalories = Integer.max(maxCalories, elf.countCalories());
        }
        return maxCalories;
    }

    @Override
    public Object getPart2Solution() {
        final List<Elf> elves = getInput();

        Collections.sort(elves, new SortbyCalories());

        int topThreeCalories = elves.get(0).countCalories() + elves.get(1).countCalories() + elves.get(2).countCalories();

        return topThreeCalories;

    }

        class SortbyCalories implements Comparator<Elf>
        {
            public int compare(Elf a, Elf b)
            {
                return b.countCalories() - a.countCalories();
            }
        }

    private static class Elf {
        public final List<Integer> items;

        public Elf(List<Integer> items) {
            this.items = items;
        }

        public Integer countCalories() {
            int calories = 0;
            for (Integer i : items) {
                calories += i;
            }
            return calories;
        }
    }

    private List<Elf> getInput() {
        final List<String> input = getInputLines();
        final List<Elf> elves = new ArrayList<Elf>();

        List<Integer> items = new ArrayList<Integer>();
        for (String i : input) {

            if (i.isEmpty()) {
                elves.add(new Elf(items));
                items = new ArrayList<Integer>();
            }
            else
            {
                items.add(Integer.parseInt(i));
            }
        }
        elves.add(new Elf(items));

        return elves;
    }
}
