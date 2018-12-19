package net.wojtekk.aoc2018.days.day12

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day12/input.txt"
    val input = readLines(path)
            .map { it.split(Regex("[^.#]+")).filter { it.isNotEmpty() } }
    var plants = input[0][0]

    val generations = 20
    val mutations = input.drop(1).map { Pair(it[0], it[1]) }
    var indexZero = 0

    for (g in 1 .. generations) {
        val normPlants = ".....$plants....."
        indexZero += 3

        var newPlants = ""
        for (c in 2 until normPlants.length - 2) {
            val n = mutations.firstOrNull { it.first == normPlants.substring(c - 2, c + 3) }
            newPlants += n?.second ?: normPlants[c]
        }
        plants = newPlants
    }

    val res = plants.mapIndexed { index, c -> if (c == '#') index-indexZero else 0 }.sum()

    System.out.println("Answer: $res")
}
