package net.wojtekk.aoc2018.days.day09

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.util.LinkedList

@kotlin.ExperimentalUnsignedTypes
fun main() {
    val path = "day09/input.txt"
    val (players, lastMarble) = readLines(path)
            .map { it.split(" ") }
            .map { Pair(it[0].toInt(), it[6].toInt()) }
            .first()

    val points = UIntArray(players) { 0U }
    val circle = LinkedList<Int>(listOf(0))

    (1..lastMarble * 100).forEach { marble ->
        if (marble.rem(23) == 0) {
            (0 until 7).forEach { circle.addFirst(circle.removeLast()) }
            points[(marble - 1).rem(players)] += (marble + circle.removeFirst()).toUInt()
        } else {
            (0 until 2).forEach { circle.addLast(circle.removeFirst()) }
            circle.addFirst(marble)
        }
    }

    val res = points.max()
    System.out.println("Answer: $res")
}
