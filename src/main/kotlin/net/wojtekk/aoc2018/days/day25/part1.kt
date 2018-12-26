package net.wojtekk.aoc2018.days.day25

import net.wojtekk.aoc2018.utils.FileHelper.readLines


class Constellation(val points: MutableSet<Point> = emptySet<Point>().toMutableSet())

data class Point(val x: Int, val y: Int, val z: Int, val t: Int)

object Day25p1 {
    val maxDistance = 3

    val memory: MutableMap<Point, Constellation> = emptyMap<Point, Constellation>().toMutableMap()

    fun main() {
        val path = "day25/input.txt"
        val input = readLines(path)

        val points = input
                .map { it.split(",").map(String::toInt) }
                .map { Point(it[0], it[1], it[2], it[3]) }

        points.forEach { memory[it] = Constellation(setOf(it).toMutableSet()) }

        for (i in 0 until points.size)
            for (j in (i + 1) until points.size) {
                if (distance(points[i], points[j]) <= maxDistance)
                    makeConstellation(points[i], points[j])
            }

        val res = memory.asSequence().groupBy { it.value }.keys.size

        System.out.println("Answer: $res")
    }


    fun makeConstellation(p1: Point, p2: Point) {
        val c1 = memory[p1]!!
        val c2 = memory[p2]!!

        c2.points.addAll(c1.points)
        c1.points.forEach { memory[it] = c2 }
    }

    fun distance(p1: Point, p2: Point): Int {
        return listOf(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z, p1.t - p2.t)
                .map(Math::abs)
                .sum()
    }
}


fun main() {
    Day25p1.main()
}
