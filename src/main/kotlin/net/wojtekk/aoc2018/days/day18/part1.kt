package net.wojtekk.aoc2018.days.day18

import net.wojtekk.aoc2018.utils.FileHelper.readLines

object Day18p1 {
    fun main() {
        val path = "day18/input.txt"
        var map = readLines(path).map { it.split("") }

        map.forEach { System.out.println(it.joinToString("")) }

        for (round in 1..10) {
            val newMap = map.map { it.toMutableList() }
            for (y in 0 until map.size) {
                for (x in 0 until map[y].size) {
                    val c = map[y][x]
                    val n = neighbours(map, x, y)
                    val s = n.groupingBy { it }.eachCount()
                    if (c == "." && s.getOrDefault("|", 0) >= 3) {
                        newMap[y][x] = "|"
                    } else if (c == "|" && s.getOrDefault("#", 0) >= 3) {
                        newMap[y][x] = "#"
                    } else if (c == "#" && (s.getOrDefault("#", 0) == 0 || s.getOrDefault("|", 0) == 0)) {
                        newMap[y][x] = "."
                    }
                }
            }

            map = newMap
        }

        val numOfTries = map.flatten().filter { it == "|" }.count()
        val numOflumberyards = map.flatten().filter { it == "#" }.count()
        val res = numOfTries * numOflumberyards

        System.out.println("Answer: $res")
    }

    fun neighbours(map: List<List<String>>, x: Int, y: Int): List<String> {
        return listOf(map.getOrNull(y - 1)?.getOrNull(x - 1),
                map.getOrNull(y - 1)?.getOrNull(x),
                map.getOrNull(y - 1)?.getOrNull(x + 1),
                map.getOrNull(y)?.getOrNull(x - 1),
                map.getOrNull(y)?.getOrNull(x + 1),
                map.getOrNull(y + 1)?.getOrNull(x - 1),
                map.getOrNull(y + 1)?.getOrNull(x),
                map.getOrNull(y + 1)?.getOrNull(x + 1))
                .filterNotNull()
    }
}

fun main() {
    Day18p1.main()
}
