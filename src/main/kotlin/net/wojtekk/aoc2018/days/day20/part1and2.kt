package net.wojtekk.aoc2018.days.day20

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.util.*

// Solution inspired by:
// https://www.reddit.com/r/adventofcode/comments/a7uk3f/2018_day_20_solutions/ec66tkm/
// I've completely couldn't find idea how to do that :) This way is so easy and beautiful !

object Day20p2 {
    fun main() {
        val path = "day20/input.txt"
        val map = readLines(path).first().drop(1).dropLast(1)

        val acc = ArrayDeque<Pair<Int, Int>>()
        val distance = mutableMapOf<Pair<Int, Int>, Int>()

        var room = Pair(0, 0)
        var prevRoom = room

        for (c in map) {
            when (c) {
                '(' -> acc.push(room)
                ')' -> room = acc.pop()
                '|' -> room = acc.peek()
                else -> {
                    val delta = getDeltas(c)
                    room = Pair(room.first + delta.first, room.second + delta.second)
                    distance[room] = Math.min(
                            distance[room] ?: Int.MAX_VALUE,
                            (distance[prevRoom] ?: 0) + 1)
                }
            }
            prevRoom = room
        }

        val res1 = distance.values.max()
        System.out.println("Answer 1: $res1")

        val res2 = distance.values.filter { it >= 1000 }.size
        System.out.println("Answer 2: $res2")
    }

    fun getDeltas(direction: Char) = when (direction) {
        'N' -> Pair(0, -1)
        'E' -> Pair(1, 0)
        'S' -> Pair(0, 1)
        'W' -> Pair(-1, 0)
        else -> throw Exception("Unknown direction")
    }
}

fun main() {
    Day20p2.main()
}
