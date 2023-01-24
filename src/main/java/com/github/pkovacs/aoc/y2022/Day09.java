package com.github.pkovacs.aoc.y2022;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.Cell;

public class Day09 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 2));
        System.out.println("Part 2: " + solve(lines, 10));
    }

    private static int solve(List<String> lines, int ropeLength) {
        var rope = new Cell[ropeLength];
        Arrays.fill(rope, new Cell(0, 0));

        var visited = new HashSet<Cell>();
        for (var line : lines) {
            var cnt = Integer.parseInt(line.substring(2));
            for (int k = 0; k < cnt; k++) {
                rope[0] = rope[0].neighbor(line.charAt(0));
                for (int i = 1; i < rope.length; i++) {
                    if (!rope[i].equals(rope[i - 1])
                            && rope[i].extendedNeighbors().noneMatch(rope[i - 1]::equals)) {
                        rope[i] = rope[i].extendedNeighbors()
                                .min(Comparator.comparing(rope[i - 1]::dist1)).orElseThrow();
                    }
                }
                visited.add(rope[rope.length - 1]);
            }
        }

        return visited.size();
    }

}
