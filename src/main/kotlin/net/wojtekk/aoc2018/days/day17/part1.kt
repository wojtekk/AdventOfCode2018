package net.wojtekk.aoc2018.days.day17

import net.wojtekk.aoc2018.utils.FileHelper.readLines

object Day17p1 {
    fun main() {
        val path = "day17/input.txt"
        val input = readLines(path)
                .map { it.split(Regex("[^xy0-9]+")) }

        val spring = Pair(500, 0) // x, y

        val minY = 0
        val maxY = input
                .map { if (it[0] == "y") it[1].toInt() else Math.max(it[3].toInt(), it[4].toInt()) }
                .max()!!

        val minX = input
                .map { if (it[0] == "x") it[1].toInt() else Math.min(it[3].toInt(), it[4].toInt()) }
                .min()
                .let { it!! - 1 }
        val maxX = input
                .map { if (it[0] == "x") it[1].toInt() else Math.max(it[3].toInt(), it[4].toInt()) }
                .max()
                .let { it!! + 1 }

        val offsetX = minX
        val sizeX = maxX - minX
        val sizeY = maxY + 1

        System.out.println("Configuration: $minX - $maxX ($sizeX) / $minY - $maxY ($sizeY) , $spring")

        val ground = Array(sizeY) { CharArray(sizeX) { ' ' } }

        input.forEach {
            when (it[0]) {
                "x" -> for (y in it[3].toInt()..it[4].toInt()) ground[y][it[1].toInt() - offsetX] = '#'
                "y" -> for (x in it[3].toInt()..it[4].toInt()) ground[it[1].toInt()][x - offsetX] = '#'
                else -> throw IllegalArgumentException("Unknown property")
            }
        }

        ground[spring.second][spring.first - offsetX] = '+'

        ground.forEach { System.out.println(it.joinToString("")) }
    }
}

fun main() {
    Day17p1.main()
}
