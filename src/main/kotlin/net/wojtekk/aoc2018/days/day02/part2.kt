package net.wojtekk.aoc2018.days.day02

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day02/input.txt";
    val listOfIds = readLines(path)

    val boxIds = listOfIds.mapIndexed { index, id ->
        listOfIds.takeLast(listOfIds.size - index)
            .filter { comp(id, it) == 1 }
            .map { Pair(id, it) }
    }
        .flatten()
        .first()


    val res = boxIds.first.zip(boxIds.second)
        .filter { it.first == it.second }
        .joinToString("") { it.first.toString() }

    System.out.println("Answer: $res")
}

fun comp(id1: String, id2: String): Int {
    return id1.zip(id2)
        .filter { it.first != it.second }
        .count()
}
