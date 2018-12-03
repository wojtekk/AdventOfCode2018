package net.wojtekk.aoc2018.days.day02

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day02/input.txt";
    val res = readLines(path)
        .map(String::toList)
        .flatMap { w -> w.groupingBy { it }.eachCount().filter { it.value in 2..3 }.values.toSet() }
        .groupingBy { it }.eachCount().values
        .fold(1) { acc, it -> acc * it }
    System.out.println("Answer: $res")
}
