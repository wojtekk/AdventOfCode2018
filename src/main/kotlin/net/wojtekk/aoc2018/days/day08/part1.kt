package net.wojtekk.aoc2018.days.day08

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day08/input.txt"
    var registry = readLines(path)
            .flatMap { it.split(" ") }
            .map(String::toInt)

    var states = mutableListOf(Pair(1, 0))
    var checksum = 0

    do {
        if (states.last().first == 0) {
            checksum += registry.take(states.last().second).sum()
            registry = registry.drop(states.last().second)
            states = states.dropLast(1).toMutableList()
        } else {
            states.set(states.lastIndex, states.last().copy(first = states.last().first - 1))
            states.add(Pair(registry[0], registry[1]))
            registry = registry.drop(2)
        }
    } while (registry.isNotEmpty())

    System.out.println("Answer: $checksum")
}
