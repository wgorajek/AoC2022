package com.wgorajek;


import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;


public class Day25 extends Solution {
    @Override
    public Object getPart1Solution() {
        var input = getInput();
        long sum = 0l;
        for (var line : input) {
            sum += SnafuToInt(line);
        }
        return IntToSnafu(sum);
    }

    private long SnafuToInt(String line) {
        var result = 0l;
        for (var i = line.length() - 1; i >= 0; i--) {
            var numberC = line.charAt(i);
            var number = 0l;
            if (numberC == '=') {
                number = -2l;
            } else if (numberC == '-') {
                number = -1l;
            } else if (numberC == '0') {
                number = 0l;
            } else if (numberC == '1') {
                number = 1l;
            } else if (numberC == '2') {
                number = 2l;
            }
            result += number * pow(5, line.length()-i-1);
        }
        return result;
    }

    private String IntToSnafu(long number) {
        var result = "";
        var i = 0;
        while (number > 0) {
            var tmp = number;
            var tmp2 = pow(5, i);
            number = number + 2;
            var modResult = floorMod(number, 5);
            var divResult = floorDiv(number, 5);
            if (modResult == 0) {
                result = result + "=";
            } else if (modResult == 1) {
                result = result + "-";
            } else if (modResult == 2) {
                result = result + "0";
            } else if (modResult == 3) {
                result = result + "1";
            } else if (modResult == 4) {
                result = result + "2";
            }
            number = divResult;

        }
        var res2 = "";
        for (var k = result.length()-1; k >=0 ; k--) {
            res2 += result.substring(k, k+1);
        }

        return res2;
    }

    @Override
    public Object getPart2Solution() {


        return 0;
    }

    private List<String> getInput() {
        final List<String> input = getInputLines();

        return input;
    }
}
