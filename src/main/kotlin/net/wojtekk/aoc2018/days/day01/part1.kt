package net.wojtekk.aoc2018.days.day01

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day01/input.txt";
    val res = readLines(path)
        .map { Integer.valueOf(it, 10) }
        .fold(0) { acc, i -> acc + i }
    System.out.println("Answer: $res")
}
