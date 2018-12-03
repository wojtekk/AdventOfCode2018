package net.wojtekk.aoc2018.days.day01

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun main() {
    val path = "day01/input.txt"
    val registry: MutableSet<Int> = mutableSetOf()
    var sum = 0

    val res = readLines(path)
        .map(String::toInt)
        .asSequence()
        .repeat()
        .map {
            sum += it
            sum
        }
        .first { !registry.add(it) }

    System.out.println("Answer: $res")
}
