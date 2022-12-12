package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class Day11 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<Monkey> monkeys = getInput();
        long round = 1;
        for (round = 1; round <= 20; round++) {
            for (var monkey : monkeys) {
                monkey.executeTurn();
            }
        }

        long firstMax = 0;
        long secondMax = 0;
        for (var monkey : monkeys) {
            firstMax = Long.max(monkey.inspectedItems, firstMax);
        }
        for (var monkey : monkeys) {
            if (firstMax != monkey.inspectedItems) {
                secondMax = Long.max(monkey.inspectedItems, secondMax);
            }
        }
        return firstMax * secondMax;
    }

    @Override
    public Object getPart2Solution() {
        List<Monkey> monkeys = getInput();
        long round = 1;
        for (round = 1; round <= 10000; round++) {
            for (var monkey : monkeys) {
                monkey.executeTurn();
            }
        }

        long result = 0;

        long firstMax = 0;
        long secondMax = 0;
        for (var monkey : monkeys) {
            firstMax = Long.max(monkey.inspectedItems, firstMax);
        }
        for (var monkey : monkeys) {
            if (firstMax != monkey.inspectedItems) {
                secondMax = Long.max(monkey.inspectedItems, secondMax);
            }
        }
        return firstMax * secondMax;
    }


    interface Operation {
        Long run(Long number);
    }

    interface Test {
        Boolean run(Long number);
    }

    class MyTest implements Test {
        Long divisibleBy;

        @Override
        public Boolean run(Long number) {
            return Math.floorMod(number, divisibleBy) == 0;
        }

        public MyTest(Long divisibleBy) {
            this.divisibleBy = divisibleBy;
        }
    }

    class MyOperation implements Operation {
        String firstArg;
        String secondArg;
        String operator;

        @Override
        public Long run(Long number) {
            Long result = 0l;
            Long x;
            Long y;

            if (firstArg.equals("old")) {
                x = number;
            } else {
                x = Long.parseLong(firstArg);
            }
            if (secondArg.equals("old")) {
                y = number;
            } else {
                y = Long.parseLong(secondArg);
            }
            if (operator.equals("+")) {
                result = x + y;
            } else {
                result = x * y;
            }

            return result;
        }

        public MyOperation(String firstArg, String operator, String secondArg) {
            this.firstArg = firstArg;
            this.secondArg = secondArg;
            this.operator = operator;
        }

    }

    interface StringFunction {
        String run(String str);
    }

    private static class Monkey {
        Queue<Long> items;
        Operation operation;
        long operationValue;
        Monkey throwToIfTrue;
        Monkey throwToIfFalse;
        Test test;
        long inspectedItems = 0l;
        Long modulo = 0l;

        public Monkey(Queue<Long> items, Operation operation, Test test) {
            this.items = items;
            this.operation = operation;
            this.test = test;
        }

        public void executeTurn() {
            while (!items.isEmpty()) {
                inspectedItems++;
                var item = items.remove();
                item = operation.run(item);
                if (modulo != 0) {
                    item = Math.floorMod(item, modulo);
                }
                if (test.run(item)) {
                    throwToIfTrue.items.add(item);
                } else {
                    throwToIfFalse.items.add(item);
                }
            }
        }
    }

    private List<Monkey> getInput() {
        final List<String> input = getInputLines();
        final List<Monkey> monkeys = new ArrayList<Monkey>();
        final List<LinkedList<Long>> itemsList = new ArrayList<LinkedList<Long>>();
        var modulo = 1l;
        for (var i = 0; i < input.size(); i++) {
            if (input.get(i).contains("Monkey")) {
//                input.get(0).substring(7,8);
                var items = new LinkedList<Long>();
                for (var number : input.get(i + 1).split(":")[1].split(",")) {
                    items.add(Long.parseLong(number.trim()));
                }
                itemsList.add(items);
                var operationLine = input.get(i + 2);
                operationLine = operationLine.split("=")[1].trim();
                var operationArray = operationLine.split(" ");
                Operation operation = new MyOperation(operationArray[0].trim(), operationArray[1].trim(), operationArray[2].trim());

                var testLine = input.get(i + 3);
                testLine = testLine.split("by")[1];
                modulo *= Long.parseLong(testLine.trim());
                var test = new MyTest(Long.parseLong(testLine.trim()));
                var monkey = new Monkey(items, operation, test);
                monkeys.add(monkey);
            }
        }
        var j = 0;
        for (var i = 0; i < input.size(); i++) {
            if (input.get(i).contains("Monkey")) {

                var ifTrueLine = input.get(i + 4);
                var ifTrueThrow = Integer.parseInt(ifTrueLine.split("monkey")[1].trim());
                monkeys.get(j).throwToIfTrue = monkeys.get(ifTrueThrow);
                var ifFalseLine = input.get(i + 5);
                var ifFalseThrow = Integer.parseInt(ifFalseLine.split("monkey")[1].trim());
                monkeys.get(j).throwToIfFalse = monkeys.get(ifFalseThrow);
                monkeys.get(j).modulo = modulo;
                j++;

            }
        }

        return monkeys;
    }
}
