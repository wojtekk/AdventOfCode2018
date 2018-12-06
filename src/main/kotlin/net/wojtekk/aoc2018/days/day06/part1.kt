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

    val maxX = points.map { it.x }.max()!!
    val maxY = points.map { it.y }.max()!!

    val surface = Array(maxX) { Array<BasePoint>(maxY) { EmptyPoint } }

    (0 until maxX).forEach { x ->
        (0 until maxY).forEach { y ->
            val owners =
                points.map { it to distance(Point(x, y), it) }
                    .sortedBy { it.second }
                    .take(2)
                    .zipWithNext()
                    .first()
            surface[x][y] = if (owners.first.second == owners.second.second) {
                CommonPoint
            } else {
                owners.first.first
            }
        }
    }

    val infinity = mutableSetOf<BasePoint>()
    (0 until maxX).forEach { x ->
        infinity.add(surface[x][0])
        infinity.add(surface[x][maxY - 1])
    }
    (0 until maxY).forEach { y ->
        infinity.add(surface[0][y])
        infinity.add(surface[maxX - 1][y])
    }

    val area = mutableMapOf<Point, Int>()
    surface.forEach { r ->
        r.forEach { c ->
            if (c !in infinity && c is Point) {
                area.putIfAbsent(c, 0)
                area.computeIfPresent(c) { _, v -> v + 1 }
            }
        }
    }

    val res = area.asSequence().map { it.value }.max()

    System.out.println("Answer: $res")
}


fun distance(a: Point, b: Point) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
