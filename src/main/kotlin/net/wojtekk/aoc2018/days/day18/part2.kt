package net.wojtekk.aoc2018.days.day18

import net.wojtekk.aoc2018.utils.FileHelper.readLines

object Day18p2 {
    fun main() {
        val path = "day18/input.txt"
        var map = readLines(path).map { it.split("") }
        val maxIterations = 1000000000
        var round = 0
        val powerHistory = mutableListOf<Int>()
        val history = mutableListOf<List<List<String>>>()

        do {
            history.add(map)
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

            val numOfTries = map.flatten().filter { it == "|" }.count()
            val numOflumberyards = map.flatten().filter { it == "#" }.count()
            powerHistory.add(numOfTries * numOflumberyards)

            map = newMap
        } while (++round < maxIterations && !history.contains(map))

        val firstRepetition = history.indexOf(map)
        val cycle = round - firstRepetition
        val remainingRounds = (maxIterations - round).rem(cycle)
        val res = powerHistory[firstRepetition + remainingRounds]

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
    Day18p2.main()
}
