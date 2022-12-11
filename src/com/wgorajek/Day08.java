package com.wgorajek;


import java.util.ArrayList;
import java.util.List;

public class Day08 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<ArrayList<Tree>> forest = getInput();
        int result = 0;
        int ySize = forest.get(0).size() - 1;
        int xSize = forest.size() - 1;
        for (int x = 0; x <= xSize ; x++) {
            int previousMaxTreeHeight = - 1;
            for(int y = 0; y <= ySize; y++) {
                var tree = forest.get(y).get(x);
                if (previousMaxTreeHeight < tree.height) {
                    tree.visible = true;
                    previousMaxTreeHeight = tree.height;
                }
            }
        }

        for (int x = 0; x <= xSize ; x++) {
            int previousMaxTreeHeight = - 1;
            for(int y = ySize; y >= 0; y--) {
                var tree = forest.get(y).get(x);
                if (previousMaxTreeHeight < tree.height) {
                    tree.visible = true;
                    previousMaxTreeHeight = tree.height;
                }
            }
        }


        for(int y = 0; y <= ySize; y++) {
            int previousMaxTreeHeight = - 1;
            for (int x = 0; x <= xSize ; x++) {
                var tree = forest.get(y).get(x);
                if (previousMaxTreeHeight < tree.height) {
                    tree.visible = true;
                    previousMaxTreeHeight = tree.height;
                }
            }
        }

        for(int y = 0; y <= ySize; y++) {
            int previousMaxTreeHeight = - 1;
            for (int x = xSize; x >= 0 ; x--) {
                var tree = forest.get(y).get(x);
                if (previousMaxTreeHeight < tree.height) {
                    tree.visible = true;
                    previousMaxTreeHeight = tree.height;
                }
            }
        }
        for (var trees: forest) {
            for(var tree: trees) {
                if (tree.visible) {
                    result++;
                }
            }
        }

        return result;
    }

    @Override
    public Object getPart2Solution() {
        ArrayList<ArrayList<Tree>> forest = getInput();
        int result = 0;
        int ySize = forest.get(0).size() - 1;
        int xSize = forest.size() - 1;
        for (int x = 0; x <= xSize ; x++) {
            for(int y = 0; y <= ySize; y++) {
                int scenicScore = 1;
                var tree = forest.get(y).get(x);
                int viewLength = 0;
                for(int i = x + 1; i <= xSize; i++) {
                    viewLength++;
                    if (tree.height <= forest.get(y).get(i).height) {
                        break;
                    }
                }
                scenicScore *= viewLength;
                viewLength = 0;
                for(int i = x - 1; i >= 0; i--) {
                    viewLength++;
                    if (tree.height <= forest.get(y).get(i).height) {
                        break;
                    }
                }
                scenicScore *= viewLength;
                viewLength = 0;
                for(int j = y + 1; j <= ySize; j++) {
                    viewLength++;
                    if (tree.height <= forest.get(j).get(x).height) {
                        break;
                    }
                }
                scenicScore *= viewLength;
                viewLength = 0;
                for(int j = y - 1; j >= 0; j--) {
                    viewLength++;
                    if (tree.height <= forest.get(j).get(x).height) {
                        break;
                    }
                }
                scenicScore *= viewLength;
                result = Integer.max(scenicScore, result);

            }
        }


        return result;
    }

    private static class Tree {
        int height;
        boolean visible;
        public Tree(int height) {
            this.height = height;
            visible = false;
        }
    }



    private ArrayList<ArrayList<Tree>> getInput() {
        final List<String> input = getInputLines();
        ArrayList<ArrayList<Tree>> forest = new ArrayList<ArrayList<Tree>>();
        for (String line: input) {
            var trees = new ArrayList<Tree>();
            for (var treeHeight: line.toCharArray()) {
                var tree = new Tree(Integer.parseInt(String.valueOf(treeHeight)));
                trees.add(tree);
            }
            forest.add(trees);
        }
        return forest;
    }
}
