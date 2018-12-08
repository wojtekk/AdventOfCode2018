package net.wojtekk.aoc2018.days.day08

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day08/input.txt"
    var registry = readLines(path)
            .flatMap { it.split(" ") }
            .map(String::toInt)

    var states = mutableListOf(Triple(1, 0, mutableListOf<Int>()))
    var checksum = 0

    do {
        if (states.last().first == 0) {
            val meta = registry.take(states.last().second)
            val nodeValue = if (states.last().third.isNotEmpty()) {
                meta.map { states.last().third.getOrElse(it - 1) { 0 } }.sum()
            } else {
                meta.sum()
            }
            registry = registry.drop(states.last().second)
            if(registry.isEmpty()) {
                checksum = nodeValue
            }
            states = states.dropLast(1).toMutableList()
            states.last().third.add(nodeValue)
        } else {
            states.set(states.lastIndex, states.last().copy(first = states.last().first - 1))
            states.add(Triple(registry[0], registry[1], mutableListOf()))
            registry = registry.drop(2)
        }
    } while (registry.isNotEmpty())

    System.out.println("Answer: $checksum")
}
