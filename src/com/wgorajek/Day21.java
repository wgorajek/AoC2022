package com.wgorajek;


import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day21 extends Solution {
    @Override
    public Object getPart1Solution() {
        var monkeys = getInput();

        var monkeysMap = new HashMap<String, Monkey>();
        for (var monkey : monkeys) {
            monkeysMap.put(monkey.name, monkey);
        }

        var allHasValue = false;
        while (!allHasValue) {
            allHasValue = true;
            for (var monkey : monkeys) {
                if (!monkey.hasValue && monkey.canMakeOperation()) {
                    monkey.makeOperation();
                }
                allHasValue = allHasValue && monkey.hasValue;
            }
        }
        return monkeysMap.get("root").value;
    }

    @Override
    public Object getPart2Solution() {
        var monkeys = getInput();

        var monkeysMap = new HashMap<String, Monkey>();
        for (var monkey : monkeys) {
            monkeysMap.put(monkey.name, monkey);
        }

        var humnMonkey = monkeysMap.get("humn");
        var rootMonkey = monkeysMap.get("root");
        humnMonkey.value = 0;
        humnMonkey.hasValue = false;


        int countedValue = 1;
        while (countedValue > 0) {
            countedValue = 0;
            for (var monkey : monkeys) {
                if (!monkey.hasValue && monkey.canMakeOperation()) {
                    monkey.makeOperation();
                    countedValue++;
                }
            }
        }
        Monkey monkey;
        if (!rootMonkey.firstArg.hasValue) {
            monkey = rootMonkey.firstArg;
            monkey.value = rootMonkey.secondArg.value;
        } else {
            monkey = rootMonkey.secondArg;
            monkey.value = rootMonkey.firstArg.value;
        }


        while (humnMonkey.value == 0) {
            Monkey monkeyWithValue;
            Monkey monkeyWithoutValue;
            boolean withValueIsFirstArg;
            if (monkey.firstArg.hasValue) {
                monkeyWithValue = monkey.firstArg;
                monkeyWithoutValue = monkey.secondArg;
                withValueIsFirstArg = true;
            } else {
                monkeyWithValue = monkey.secondArg;
                monkeyWithoutValue = monkey.firstArg;
                withValueIsFirstArg = false;
            }
            monkeyWithoutValue.value = monkey.reverseOperation(monkey, monkeyWithValue, withValueIsFirstArg);
            monkey = monkeyWithoutValue;

        }


        return humnMonkey.value;

    }


    public class Monkey {
        long value = 0l;
        boolean hasValue = false;
        String name;
        String operation = "";
        String firstArgName = "";
        String secondArgName = "";
        Monkey firstArg = null;
        Monkey secondArg = null;

        public Monkey(String name) {
            this.name = name;
        }

        public void setValue(long value) {
            this.value = value;
            this.hasValue = true;
        }

        public boolean canMakeOperation() {
            if (operation.isEmpty()) {
                return false;
            } else {
                return firstArg.hasValue && secondArg.hasValue;
            }
        }

        public void makeOperation() {
            if (!hasValue && !operation.equals("")) {
                if (firstArg.hasValue && secondArg.hasValue) {
                    if (operation.endsWith("+")) {
                        setValue(firstArg.value + secondArg.value);
                    } else if (operation.endsWith("*")) {
                        setValue(firstArg.value * secondArg.value);
                    } else if (operation.endsWith("-")) {
                        setValue(firstArg.value - secondArg.value);
                    } else if (operation.endsWith("/")) {
                        setValue(firstArg.value / secondArg.value);
                    }
                }
            }
        }

        public long reverseOperation(Monkey monkey, Monkey monkeyWithValue, boolean withValueIsFirstArg) {

            long value = 0l;
            if (operation.endsWith("+")) {
                value = monkey.value - monkeyWithValue.value;
            } else if (operation.endsWith("*")) {
                value = monkey.value / monkeyWithValue.value;
            } else if (operation.endsWith("-")) {
                if (withValueIsFirstArg) {
                    value = monkeyWithValue.value - monkey.value;
                } else {
                    value = monkey.value + monkeyWithValue.value;
                }
            } else if (operation.endsWith("/")) {
                if (withValueIsFirstArg) {
                    value = monkeyWithValue.value / monkey.value;
                } else {
                    value = monkey.value * monkeyWithValue.value;
                }
            }
            return value;

        }
    }


    private List<Monkey> getInput() {
        final List<String> input = getInputLines();
        var monkeys = new ArrayList<Monkey>();

        Pattern pattern = Pattern.compile("(....) (.) (....)");
        for (var line : input) {
            var monkey = new Monkey(line.substring(0, 4));
            monkeys.add(monkey);
            var str = line.substring(6);
            Matcher m = pattern.matcher(str);
            if (m.matches()) {
                monkey.firstArgName = m.group(1);
                monkey.secondArgName = m.group(3);
                monkey.operation = m.group(2);
            } else {
                monkey.setValue(Integer.parseInt(str));
            }
        }
        var monkeysMap = new HashMap<String, Monkey>();
        for (var monkey : monkeys) {
            monkeysMap.put(monkey.name, monkey);
        }
        for (var monkey : monkeys) {
            if (!monkey.firstArgName.isEmpty()) {
                monkey.firstArg = monkeysMap.get(monkey.firstArgName);
            }
            if (!monkey.secondArgName.isEmpty()) {
                monkey.secondArg = monkeysMap.get(monkey.secondArgName);
            }
        }

        return monkeys;
    }
}
