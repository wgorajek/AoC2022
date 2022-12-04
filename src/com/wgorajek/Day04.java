package com.wgorajek;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Day04 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<ElfsPair> elfPairs = getInput();
        int sum = 0;
        for (var elfPair: elfPairs) {
            if ((elfPair.elf1Begin <= elfPair.elf2Begin && elfPair.elf1End >= elfPair.elf2End)
            ||  (elfPair.elf1Begin >= elfPair.elf2Begin && elfPair.elf1End <= elfPair.elf2End)
            ) {
                sum++;
            }

        }
        return sum;
    }

    @Override
    public Object getPart2Solution() {
        final List<ElfsPair> elfPairs = getInput();
        int sum = 0;
        for (var elfPair: elfPairs) {
            if ((elfPair.elf1Begin >= elfPair.elf2Begin && elfPair.elf1Begin <= elfPair.elf2End)
                    ||  (elfPair.elf1End >= elfPair.elf2Begin && elfPair.elf1End <= elfPair.elf2End)
                    ||  (elfPair.elf1Begin <= elfPair.elf2Begin && elfPair.elf1End >= elfPair.elf2End)
            ) {
                sum++;
            }
        }
        return sum;
    }


    private static class ElfsPair {
        public final int elf1Begin;
        public final int elf1End;
        public final int elf2Begin;
        public final int elf2End;

        public ElfsPair(int elf1Begin, int elf1End, int elf2Begin, int elf2End) {
            this.elf1Begin = elf1Begin;
            this.elf1End = elf1End;
            this.elf2Begin = elf2Begin;
            this.elf2End = elf2End;
        }

    }

    private List<ElfsPair> getInput() {
        final List<String> input = getInputLines();
        final List<ElfsPair> elfPairs = new ArrayList<ElfsPair>();
        //2-9,9-51
        Pattern pattern = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");
        for (String i : input) {
            Matcher m = pattern.matcher(i);
            m.matches();
            elfPairs.add( new ElfsPair(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
        }
        return elfPairs;
    }
}
