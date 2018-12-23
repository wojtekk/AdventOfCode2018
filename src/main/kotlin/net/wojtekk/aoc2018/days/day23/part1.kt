package net.wojtekk.aoc2018.days.day23

import net.wojtekk.aoc2018.utils.FileHelper.readLines

data class Position(val x: Int, val y: Int, val z: Int)
data class Robot(val position: Position, val range: Int)

object Day23p1 {
    fun main() {
        val path = "day23/input.txt"
        var robots = readLines(path)
                .map { it.split(Regex("[^-0-9]+")).drop(1).map(String::toInt) }
                .map { Robot(Position(it[0], it[1], it[2]), it[3]) }

        val strongest = robots.maxBy { it.range }!!

        val res = robots
                .map { distance(strongest.position, it.position) }
                .filter { it <= strongest.range }
                .count()

        System.out.println("Answer: $res")
    }

    fun distance(p1: Position, p2: Position): Int {
        return listOf(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z)
                .map(Math::abs)
                .sum()
    }
}

fun main() {
    Day23p1.main()
}
