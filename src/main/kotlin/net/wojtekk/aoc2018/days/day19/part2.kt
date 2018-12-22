package net.wojtekk.aoc2018.days.day19

import net.wojtekk.aoc2018.utils.FileHelper.readLines

object Day19p2 {
    fun main() {
        val path = "day19/input.txt"
        val input = readLines(path)

        val ip = input
                .take(1)
                .map { it.replace("#ip ", "") }
                .map(String::toInt).first()

        val program = input
                .drop(1)
                .map { it.split(Regex("[ ]+")) }
                .map { InstructionFactory.create(it[0], it.drop(1).map(String::toInt)) }

        var registers = Registers(1, 0, 0, 0, 0, 0)
        var p = 0
        var cnt = 0
        val maxCnt = 100000
        do {
            registers = program[p].execute(registers)
            p = registers.get(ip) + 1
            registers.set(ip, p)
            cnt++
        } while (p < program.size && cnt < maxCnt)

        // After a long long time ... and disassembly of the programme,
        // I've noticed that the programme is trying to sum all dividers of registry E
        val res = (1..registers.e)
                .filter { registers.e.rem(it) == 0 }
                .sum()

        println("Answer: $res")
    }
}

fun main() {
    Day19p2.main()
}
