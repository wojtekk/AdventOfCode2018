package net.wojtekk.aoc2018.days.day10

import net.wojtekk.aoc2018.utils.FileHelper.readLines

data class Point(var x: Int, var y: Int, val vx: Int, val vy: Int)

fun main() {
    val path = "day10/input.txt"
    val points = readLines(path)
            .map { it.split(Regex("[^-0-9]+")).subList(1, 5).map(String::toInt) }
            .map { Point(it[0], it[1], it[2], it[3]) }

    var globalMinX = Int.MAX_VALUE
    var prevGlobalMinX: Int
    var seconds = 0

    do {
        prevGlobalMinX = globalMinX
        points.map { it -> it.x += it.vx; it.y += it.vy; it }
        globalMinX = Math.min(Math.abs(points.map { it.x }.max()!! - points.map { it.x }.min()!!), globalMinX)
    } while (prevGlobalMinX > globalMinX && seconds++ < 100000)

    points.map { it -> it.x -= it.vx; it.y -= it.vy; it }
    val minX = points.map { it.x }.min()!!
    val maxX = points.map { it.x }.max()!!
    val minY = points.map { it.y }.min()!!
    val maxY = points.map { it.y }.max()!!

    var res = ""
    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            res += if (points.indexOfFirst { it.x == x && it.y == y } == -1) "." else "*"
        }
        res += "\n"
    }
    System.out.println("$res\nSeconds: $seconds ")
}
