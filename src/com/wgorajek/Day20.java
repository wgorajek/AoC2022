package com.wgorajek;



import java.util.*;
import java.util.List;


public class Day20 extends Solution {
    @Override
    public Object getPart1Solution() {
        var numbers = getInput();
        long result = 0l;

        for (var number : numbers) {
            var oldPosition = number.position;
            number.position = (number.position + number.value + 10 * (numbers.size() - 1)) % (numbers.size() - 1);
            for (var otherNumber : numbers) {
                if (otherNumber != number) {
                    if (number.position > oldPosition && number.value > 0 && ((oldPosition < otherNumber.position && otherNumber.position <= number.position)
                            || (number.position <= otherNumber.position && otherNumber.position < oldPosition))) {
                        otherNumber.position = (otherNumber.position - 1 + numbers.size()) % numbers.size();
                    }
                    if ((number.position > oldPosition) && (number.value < 0) && (oldPosition < otherNumber.position && number.position >= otherNumber.position)) {
                        otherNumber.position = (otherNumber.position - 1 + numbers.size()) % numbers.size();
                    }
                    if ((number.position < oldPosition) && number.value > 0 && (number.position <= otherNumber.position && otherNumber.position < oldPosition)) {
                        otherNumber.position = (otherNumber.position + 1 + numbers.size()) % numbers.size();
                    }
                    if ((number.position < oldPosition) && (number.value < 0) && (number.position <= otherNumber.position && oldPosition > otherNumber.position)) {
                        otherNumber.position = (otherNumber.position + 1 + numbers.size()) % numbers.size();
                    }
                }
            }

        }
        var positionOf0 = 0l;
        for (var number : numbers) {
            if (number.value == 0l) {
                positionOf0 = number.position;
            }
        }


        for (var number : numbers) {
            if (number.position == (1000 + positionOf0) % numbers.size()
                    || number.position == (2000 + positionOf0) % numbers.size()
                    || number.position == (3000 + positionOf0) % numbers.size()
            ) {
                result += number.value;
            }
        }

        return result;
    }

    @Override
    public Object getPart2Solution() {
        var numbers = getInput();
        long result = 0;
        for (var number : numbers) {
            number.value = number.value * 811589153l;
        }
long k = 0l;
        for (var j = 0; j < 10; j++) {
            for (var number : numbers) {
                var oldPosition = number.position;
                if (number.value != 0) {
                    number.position = (number.position + number.value + 8115891530l * (numbers.size() - 1)) % (numbers.size() - 1);
                }
                k++;
                for (var otherNumber : numbers) {
                    if (otherNumber != number) {
                        if (number.position > oldPosition && number.value > 0 && ((oldPosition < otherNumber.position && otherNumber.position <= number.position)
                                || (number.position <= otherNumber.position && otherNumber.position < oldPosition))) {
                            otherNumber.position = (otherNumber.position - 1 + numbers.size()) % numbers.size();
                        }
                        if ((number.position > oldPosition) && (number.value < 0) && (oldPosition < otherNumber.position && number.position >= otherNumber.position)) {
                            otherNumber.position = (otherNumber.position - 1 + numbers.size()) % numbers.size();
                        }
                        if ((number.position < oldPosition) && number.value > 0 && (number.position <= otherNumber.position && otherNumber.position < oldPosition)) {
                            otherNumber.position = (otherNumber.position + 1 + numbers.size()) % numbers.size();
                        }
                        if ((number.position < oldPosition) && (number.value < 0) && (number.position <= otherNumber.position && oldPosition > otherNumber.position)) {
                            otherNumber.position = (otherNumber.position + 1 + numbers.size()) % numbers.size();
                        }
                    }
                }
            }
        }
        var positionOf0 = 0l;
        for (var number : numbers) {
            if (number.value == 0l) {
                positionOf0 = number.position;
            }
        }


        for (var number : numbers) {
            if (number.position == (1000 + positionOf0) % numbers.size()
                    || number.position == (2000 + positionOf0) % numbers.size()
                    || number.position == (3000 + positionOf0) % numbers.size()
            ) {
                result += number.value;
//                System.out.println(number.value);
            }
        }

        return result;
    }


    public class Number {
        long position;
        long value;

        public Number(long position, long value) {
            this.position = position;
            this.value = value;
        }
    }


    private List<Number> getInput() {
        final List<String> input = getInputLines();
        var numbers = new ArrayList<Number>();
        var i = 0;
        for (var line : input) {
            numbers.add(new Number(i, Integer.parseInt(line)));
            i++;
        }

        return numbers;
    }
}
