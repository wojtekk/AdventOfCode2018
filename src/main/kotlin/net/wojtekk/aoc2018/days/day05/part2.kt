package net.wojtekk.aoc2018.days.day05

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day05/input.txt";
    val polymer = readLines(path).joinToString("").trim()
    val registry: MutableMap<Char, Int> = mutableMapOf()

    for (unit in 'a'..'z') {
        var res = fullReaction(removeUnit(polymer, unit))
        System.out.println("- ${unit.toUpperCase()}: ${res.length}")
        registry.put(unit, res.length)
    }

    val res = registry
        .asSequence()
        .map { it.value }
        .sorted()
        .first()

    System.out.println("Answer: ${res}")
}

fun removeUnit(polymer: String, unit: Char): String {
    return polymer
        .replace(unit.toLowerCase().toString(), "")
        .replace(unit.toUpperCase().toString(), "")
}

