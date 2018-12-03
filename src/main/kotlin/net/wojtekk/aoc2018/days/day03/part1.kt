package net.wojtekk.aoc2018.days.day03

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day03/input.txt";
    val res = readLines(path)
        .map { it.split(Regex("[^0-9]+")).subList(2, 6).map(String::toInt) }
        .flatMap {
            (it[0] until it[0] + it[2])
                .flatMap { r -> (it[1] until it[1] + it[3]).map { c -> Pair(r, c) } }
        }
        .groupingBy { it }.eachCount()
        .filter { it.value >= 2 }
        .count()
    System.out.println("Answer: $res")
}
