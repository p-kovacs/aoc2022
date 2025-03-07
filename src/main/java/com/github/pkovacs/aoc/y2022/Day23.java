package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.CounterMap;
import com.github.pkovacs.util.data.Direction;

public class Day23 {

    private static final Direction[] DIRECTIONS = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static long solve(List<String> lines, int part) {
        var table = new CharTable(lines);
        var elves = table.findAll('#').collect(Collectors.toSet());

        for (int round = 0; true; round++) {
            var attempts = new ArrayList<Attempt>();
            for (var elf : elves) {
                attempts.add(new Attempt(elf, move(elves, elf, round)));
            }

            var map = new CounterMap<Cell>();
            attempts.stream().map(Attempt::to).forEach(map::inc);

            if (part == 2 && map.keySet().equals(elves)) {
                return round + 1;
            }

            elves = attempts.stream().map(a -> map.get(a.to) == 1 ? a.to : a.from).collect(Collectors.toSet());

            if (part == 1 && round == 9) {
                return Cell.rowRange(elves).count() * Cell.colRange(elves).count() - elves.size();
            }
        }
    }

    private static Cell move(Set<Cell> elves, Cell elf, int round) {
        if (elf.extendedNeighbors().noneMatch(elves::contains)) {
            return elf;
        }
        for (int i = 0; i < 4; i++) {
            var dir = DIRECTIONS[(round + i) % DIRECTIONS.length];
            var next = elf.neighbor(dir);
            var check = Stream.of(next, next.neighbor(dir.rotateLeft()), next.neighbor(dir.rotateRight()));
            if (check.noneMatch(elves::contains)) {
                return next;
            }
        }
        return elf;
    }

    private record Attempt(Cell from, Cell to) {}

}
