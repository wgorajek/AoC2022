package com.wgorajek;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Solution {
    @Override
    public Object getPart1Solution() {
        List<String> input = getInput();
        int result = 0;
        Directory root = buildRoot(input);
        Directory actualDirectory = root;
        for (var d: root.getAllSubDirectories()) {
            if(d.size <= 100000) {
                result += d.size;
            }
        }
        return result;
    }

    @Override
    public Object getPart2Solution() {
        List<String> input = getInput();
        Directory root = buildRoot(input);
        int result = 0;
        int requiredSpace = root.size - 40000000;
        result = root.size;
        for (var d: root.getAllSubDirectories()) {
            if(result > d.size && requiredSpace <= d.size) {
                result = d.size;
            }
        }
        return result;
    }

    public Directory buildRoot(List<String> input) {
        Directory root = new Directory(null, "/");
        Directory actualDirectory = root;
        Pattern pattern = Pattern.compile("(\\d+) (.+)");
        for (var line : input) {
            if (line.substring(0,1).equals("$")) {
                if (line.substring(2,4).equals("cd")) {
                    if (line.substring(5).equals("/")) {
                        actualDirectory = root;
                    }
                    else if (line.substring(5).equals("..")) {
                        actualDirectory = actualDirectory.root;
                    }
                    else {
                        var ttt = actualDirectory.findSubDirectory(line.substring(5));
                        actualDirectory = actualDirectory.findSubDirectory(line.substring(5));
                    }
                }
                else if (line.substring(2,4).equals("ls")) {
                }
            } else  {
                if (line.substring(0,3).equals("dir")) {
                    actualDirectory.subdirectories.add(new Directory(actualDirectory, line.substring(4)));
                }
                else {
                    Matcher m = pattern.matcher(line);
                    m.matches();
                    actualDirectory.files.add(new File(m.group(2), Integer.parseInt(m.group(1))));
                }
            }
        }
        root.calculateSize();
        return root;
    }

    private static class File {
        String name;
        int size;
        public File(String name, int size) {
            this.size = size;
            this.name = name;
        }
    }

    private static class Directory {
        List<File> files;
        List<Directory> subdirectories;
        Directory root;
        int size;
        String name;
        public Directory(Directory root, String name) {
            this.name = name;
            this.root = root;
            size = 0;
            subdirectories = new ArrayList<Directory>();
            files = new ArrayList<File>();
        }
        public Directory findSubDirectory(String name) {
            Directory result = null;
            for (var dir: subdirectories) {
                if (dir.name.equals(name)) {
                    result = dir;
                    break;
                }
            }
            return result;
        }

        public void calculateSize() {
            for (var f: files) {
                size += f.size;
            }
            for (var d: subdirectories) {
                d.calculateSize();
                size += d.size;
            }
        }
        public List<Directory> getAllSubDirectories() {
            var result = new ArrayList<Directory>();
            for (var d: subdirectories) {
                result.addAll(d.getAllSubDirectories());
            }
            result.add(this);
            return result;
        }
    }

    private List<String> getInput() {
        final List<String> input = getInputLines();
        return input;
    }
}
