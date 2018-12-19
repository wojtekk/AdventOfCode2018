package net.wojtekk.aoc2018.days.day12

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day12/input.txt"
    val input = readLines(path)
            .map { it.split(Regex("[^.#]+")).filter { it.isNotEmpty() } }
    var plants = input[0][0]
    val mutations = input.drop(1).map { Pair(it[0], it[1]) }

    val generations = 50000000000
    var indexZero = 0

    val checkpoint = 100
    val snapshots = mutableListOf<Int>()

    var gen = 1L
    do {
        val normPlants = ".....$plants....."
        indexZero += 3

        var newPlants = ""
        for (c in 2 until normPlants.length - 2) {
            val n = mutations.firstOrNull { it.first == normPlants.substring(c - 2, c + 3) }
            newPlants += n?.second ?: normPlants[c]
        }
        plants = newPlants

        if (gen.rem(checkpoint) == 0L) snapshots.add(sum(indexZero, plants))
    } while (++gen < generations && !test(snapshots))

    val res = sum(indexZero, plants) + (generations - snapshots.size * 100) / 100 * (snapshots[snapshots.lastIndex] - snapshots[snapshots.lastIndex - 1])

    System.out.println("Answer: $res")
}

fun sum(indexZero: Int, list: String): Int {
    return list.mapIndexed { index, c -> if (c == '#') index - indexZero else 0 }.sum()
}


fun test(list: List<Int>): Boolean {
    val i = list.lastIndex
    return i >= 3 && list[i] - list[i - 1] == list[i - 1] - list[i - 2]
}
