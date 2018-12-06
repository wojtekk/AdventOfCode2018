package net.wojtekk.aoc2018.days.day06

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val maxDistance = 10000
    val path = "day06/input.txt"
    val points = readLines(path)
        .flatMap { it.split(",").map(String::trim).map(String::toInt).zipWithNext { a, b -> Point(a, b) } }

    val maxX = points.map { it.x }.max()!!
    val maxY = points.map { it.y }.max()!!

    val surface = Array(maxX) { Array<BasePoint>(maxY) { EmptyPoint } }

    (0 until maxX).forEach { x ->
        (0 until maxY).forEach { y ->
            val totalDistance = points.map { it to distance(Point(x, y), it) }.sumBy { it.second }
            if (totalDistance < maxDistance) {
                surface[x][y] = GreatPoint
            }
        }
    }

    val res = surface.map { it.filterIsInstance<GreatPoint>().count() }.sum()

    System.out.println("Answer: $res")
}
