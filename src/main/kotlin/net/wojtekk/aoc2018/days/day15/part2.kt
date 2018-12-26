package net.wojtekk.aoc2018.days.day15

import net.wojtekk.aoc2018.utils.FileHelper.readLines


object Day15p2 {
    fun main() {
        val path = "day15/input.txt"
        val input = readLines(path)

        for (i in 4..100) {
            val units = input
                    .mapIndexed { y, s -> s.mapIndexed { x, c -> Unit.fromChar(c, x, y) } }
                    .flatten()
                    .filterNotNull()
                    .map { if (it.type == "elf") it.attackPower = i; it }

            val cntOfElfs = units.count { it.type == "elf" }

            val map = input
                    .map { it.replace("G", ".").replace("E", ".") }
                    .map { it.map { c -> c == '.' }.toBooleanArray() }

            val combat = Combat(units.toMutableList(), map)

            val result = combat.fight()

            System.out.println("Answer($i=$cntOfElfs/${result.second}): ${result.first} wins, outcome: ${result.third}")
            if (result.first == "elf" && cntOfElfs == result.second) break
        }

    }
}

fun main() {
    Day15p2.main()
}
