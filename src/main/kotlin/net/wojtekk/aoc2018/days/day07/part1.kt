package net.wojtekk.aoc2018.days.day07

import net.wojtekk.aoc2018.utils.FileHelper.readLines

data class Step(val name: String, val prev: MutableSet<String>, val next: MutableSet<String>)

fun main() {
    val path = "day07/input.txt"
    val registry = mutableMapOf<String, Step>()
    readLines(path)
            .map { it.split(" ") }
            .forEach {
                registry.putIfAbsent(it[1], Step(it[1], mutableSetOf(), mutableSetOf()))
                registry.putIfAbsent(it[7], Step(it[7], mutableSetOf(), mutableSetOf()))
                registry[it[1]]!!.next.add(it[7])
                registry[it[7]]!!.prev.add(it[1])
            }

    var awaiting = registry.asSequence()
            .filter { it.value.prev.isEmpty() }
            .sortedBy { it.key }
            .map { it.value }
            .toMutableSet()

    var res = ""

    do {
        val current = awaiting.sortedBy { it.name }.first { it.prev.all { c -> res.contains(c) } }
        res += current.name
        awaiting.addAll(current.next.map { registry[it]!! })
        awaiting = awaiting.filterNot { it == current }.sortedBy { it.name }.toMutableSet()
    } while (awaiting.isNotEmpty())

    System.out.println("Answer: $res")
}
