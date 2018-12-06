package net.wojtekk.aoc2018.days.day06

import net.wojtekk.aoc2018.utils.FileHelper.readLines

sealed class BasePoint
object EmptyPoint : BasePoint()
object CommonPoint : BasePoint()
object GreatPoint : BasePoint() // for Part2
data class Point(val x: Int, val y: Int) : BasePoint()

fun main() {
    val path = "day06/input.txt"
    val points = readLines(path)
        .flatMap { it.split(",").map(String::trim).map(String::toInt).zipWithNext { a, b -> Point(a, b) } }

    val maxX = points.map { it.x }.max()!! - 1
    val maxY = points.map { it.y }.max()!! - 1

    val surface = Array(maxX + 1) { Array<BasePoint>(maxY + 1) { EmptyPoint } }
    val infinity = mutableSetOf<BasePoint>()

    (0..maxX).forEach { x ->
        (0..maxY).forEach { y ->
            val owners = points.map { it to distance(Point(x, y), it) }
                .sortedBy { it.second }
                .take(2)
            surface[x][y] = if (owners[0].second == owners[1].second) {
                CommonPoint
            } else {
                if (x == 0 || x == maxX || y == 0 || y == maxY) infinity.add(owners[0].first)
                owners[0].first
            }
        }
    }

    val res = surface.flatMap { it.asIterable() }
        .filterNot { it in infinity }
        .filterIsInstance<Point>()
        .groupingBy { it }.eachCount()
        .map { it.value }
        .max()

    System.out.println("Answer: $res")
}


fun distance(a: Point, b: Point) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
