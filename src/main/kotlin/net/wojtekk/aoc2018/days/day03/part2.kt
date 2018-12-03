package net.wojtekk.aoc2018.days.day03

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day03/input.txt";
    val claims = readLines(path)
        .map { it.split(Regex("[^0-9]+")).subList(1, 6).map(String::toInt) }
        .map { listOf(it[0], it[1], it[2] + it[4], it[1] + it[3], it[2]) }

    val res = claims.filterIndexed { i, c1 ->
        !claims.filterIndexed { j, _ -> i != j }
            .any {
                doOverlap(c1[1], c1[2], c1[3], c1[4], it[1], it[2], it[3], it[4])
            }
    }
        .map { it[0] }

    System.out.println("Answer: $res")
}

fun doOverlap(l1x: Int, l1y: Int, r1x: Int, r1y: Int, l2x: Int, l2y: Int, r2x: Int, r2y: Int): Boolean {
    return !(l1x > r2x || l2x > r1x || l1y < r2y || l2y < r1y)
}
