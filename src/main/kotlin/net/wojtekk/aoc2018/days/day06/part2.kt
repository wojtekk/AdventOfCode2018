package net.wojtekk.aoc2018.days.day06

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val maxDistance = 10000
    val path = "day06/input.txt"
    val points = readLines(path)
        .flatMap { it.split(",").map(String::trim).map(String::toInt).zipWithNext { a, b -> Point(a, b) } }

    val maxX = points.map { it.x }.max()!! - 1
    val maxY = points.map { it.y }.max()!! - 1

    var res = 0
    (0..maxX).forEach { x ->
        (0..maxY).forEach { y ->
            if (points.sumBy { distance(Point(x, y), it) } < maxDistance) res++
        }
    }

    System.out.println("Answer: $res")
}
