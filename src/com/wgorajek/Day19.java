package com.wgorajek;


import com.jogamp.opengl.GLEventListenerState;

import javax.sound.sampled.Line;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;


public class Day19 extends Solution {
    @Override
    public Object getPart1Solution() {
        var blueprints = getInput();
        int result = 0;
        for (var blueprint : blueprints) {
            var factory = new Factory(blueprint);
            factory.time = 24;
            var tmp = blueprint.number * findBest2(factory, 24);
            result += tmp;
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        var blueprints = getInput();
        int result = 1;
        for (var i = 0; i <= 2; i++) {
            var blueprint = blueprints.get(i);
            var factory = new Factory(blueprint);
            factory.time = 32;
            var tmp = findBest2(factory, 32);
            result *= tmp;
        }
        return result;
    }


    public int findBest2(Factory baseFactory, int timeLeft) {
        int result = 0;
        var factories = new ArrayList<Factory>();
        ArrayList<Factory> newFactories = new ArrayList<Factory>();
        factories.add(baseFactory);

        while (timeLeft > 0) {
            newFactories.clear();
            for (var factory : factories) {
                for (var i = 1; i <= 5; i++) {
                    var newFactory = new Factory(factory);
                    if (newFactory.canBuildRobot(i)) {
                        newFactory.incTime();
                        newFactory.buildRobot(i);
                        newFactories.add(newFactory);
                    }
                }
            }
            factories.clear();
            for (var factory : newFactories) {
                var goodFactory = factory.isValid();
                for (var factory2 : newFactories) {
                    if (goodFactory && factory != factory2 && !factory.betterThan(factory2)) {
                        goodFactory = false;
                    }
                }
                if (goodFactory) {
                    factories.add(factory);
                }
            }
            timeLeft--;
        }

        for (var factory : factories) {
            result = max(factory.geode, result);
        }

        return result;
    }

    public class Factory {

        private final BluePrint bluePrint;
        int ore = 0;
        int clay = 0;
        int obsidian = 0;
        int geode = 0;
        int oreRobots = 0;
        int clayRobots = 0;
        int obsidianRobots = 0;
        int geodeRobots = 0;
        int time = 0;
        int value = 0;

        public void incTime() {
            time--;
            ore += oreRobots;
            clay += clayRobots;
            obsidian += obsidianRobots;
            geode += geodeRobots;
        }

        public Factory(BluePrint bluePrint) {
            this.bluePrint = bluePrint;
            oreRobots = 1;
        }

        public Factory(Factory factory) {
            this.bluePrint = factory.bluePrint;
            this.ore = factory.ore;
            this.oreRobots = factory.oreRobots;
            this.clay = factory.clay;
            this.clayRobots = factory.clayRobots;
            this.obsidian = factory.obsidian;
            this.obsidianRobots = factory.obsidianRobots;
            this.geode = factory.geode;
            this.geodeRobots = factory.geodeRobots;
            this.time = factory.time;
        }

        boolean canBuildRobot(int type) {
            Cost cost;
            boolean result = false;
            if (type == 1) {
                cost = bluePrint.oreRobotCost;
            } else if (type == 2) {
                cost = bluePrint.clayRobotCost;
            } else if (type == 3) {
                cost = bluePrint.obsidianRobotCost;
            } else if (type == 4) {
                cost = bluePrint.geodeRobotCost;
            } else {
                return true;
            }

            if (cost.clay <= clay && cost.obsidian <= obsidian && cost.ore <= ore) {
                result = true;
            }
            return result;
        }

        public void buildRobot(int type) {
            if (type == 1) {
                oreRobots++;
                spendCost(bluePrint.oreRobotCost);
            } else if (type == 2) {
                clayRobots++;
                spendCost(bluePrint.clayRobotCost);
            } else if (type == 3) {
                obsidianRobots++;
                spendCost(bluePrint.obsidianRobotCost);
            } else if (type == 4) {
                geodeRobots++;
                spendCost(bluePrint.geodeRobotCost);
            }
        }

        public void spendCost(Cost cost) {
            ore -= cost.ore;
            clay -= cost.clay;
            obsidian -= cost.obsidian;
        }


        public boolean isValid() {
            var result = oreRobots <= bluePrint.maxOre && clayRobots <= bluePrint.maxClay && obsidianRobots <= bluePrint.maxObsidian; //last added
            return result;
        }

        public boolean betterThan(Factory factory2) {
            var result = true;
            if (oreRobots <= factory2.oreRobots && clayRobots <= factory2.clayRobots && obsidianRobots <= factory2.obsidianRobots && geodeRobots <= factory2.geodeRobots
                    && (ore <= factory2.ore && clay <= factory2.clay && obsidian <= factory2.obsidian && geode <= factory2.geode)) {
                result = false;
            }
            if (oreRobots == factory2.oreRobots && clayRobots == factory2.clayRobots && obsidianRobots == factory2.obsidianRobots && geodeRobots == factory2.geodeRobots
                    && (ore == factory2.ore && clay == factory2.clay && obsidian == factory2.obsidian && geode == factory2.geode)) {
                result = true;
            }
            result = result && (geode >= factory2.geode - 2);
            return result;
        }
    }

    public class Cost {
        int ore;
        int clay;
        int obsidian;

        public Cost(int ore, int clay, int obsidian) {
            this.ore = ore;
            this.clay = clay;
            this.obsidian = obsidian;
        }
    }

    public class BluePrint {
        HashSet<Point> points;
        Cost oreRobotCost;
        Cost clayRobotCost;
        Cost obsidianRobotCost;
        Cost geodeRobotCost;
        int number;
        int maxOre;
        int maxClay;
        int maxObsidian;

        public BluePrint(Cost oreRobotCost, Cost clayRobotCost, Cost obsidianRobotCost, Cost geodeRobotCost, int number) {
            this.oreRobotCost = oreRobotCost;
            this.clayRobotCost = clayRobotCost;
            this.obsidianRobotCost = obsidianRobotCost;
            this.geodeRobotCost = geodeRobotCost;
            this.number = number;

            maxOre = max(max(max(oreRobotCost.ore, clayRobotCost.ore), obsidianRobotCost.ore), geodeRobotCost.ore);
            maxClay = max(max(max(oreRobotCost.clay, clayRobotCost.clay), obsidianRobotCost.clay), geodeRobotCost.clay);
            maxObsidian = max(max(max(oreRobotCost.obsidian, clayRobotCost.obsidian), obsidianRobotCost.obsidian), geodeRobotCost.obsidian);

        }

    }


    private List<BluePrint> getInput() {
        final List<String> input = getInputLines();
        var blueprints = new ArrayList<BluePrint>();


        Pattern pattern = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
        for (var line : input) {
            Matcher m = pattern.matcher(line);
            m.matches();
            var name = Integer.parseInt(m.group(1));
            var costOre = new Cost(Integer.parseInt(m.group(2)), 0, 0);
            var costClay = new Cost(Integer.parseInt(m.group(3)), 0, 0);
            var costObsidian = new Cost(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), 0);
            var costGeode = new Cost(Integer.parseInt(m.group(6)), 0, Integer.parseInt(m.group(7)));
            blueprints.add(new BluePrint(costOre, costClay, costObsidian, costGeode, name));
        }

        return blueprints;
    }
}
