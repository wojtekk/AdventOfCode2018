package net.wojtekk.aoc2018.days.day09

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day09/input.txt"
    val (players, lastMarble) = readLines(path)
            .map { it.split(" ") }
            .map { listOf(it[0].toInt(), it[6].toInt()) }
            .first()

    val points = IntArray(players) { 0 }
    val circle = mutableListOf(0)
    var current = 0

    (1..lastMarble).forEach { marble ->
        if (marble.rem(23) == 0) {
            val player = (marble - 1).rem(players)
            current = (current + circle.size - 7).rem(circle.size)
            points[player] += marble +  circle.removeAt(current)
        } else {
            current = if (current + 2 == circle.size) circle.size else (current + 2).rem(circle.size)
            circle.add(current, marble)
        }
    }

    val res = points.max()
    System.out.println("Answer: $res")
}
