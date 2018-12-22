package net.wojtekk.aoc2018.days.day21

import net.wojtekk.aoc2018.days.day19.InstructionFactory
import net.wojtekk.aoc2018.days.day19.Registers
import net.wojtekk.aoc2018.utils.FileHelper.readLines

object Day21p1 {
    fun main() {
        val path = "day21/input.txt"
        var input = readLines(path)

        val ip = input
                .take(1)
                .map { it.replace("#ip ", "") }
                .map(String::toInt).first()

        val program = input
                .drop(1)
                .map { it.split(Regex("[ ]+")) }
                .map { InstructionFactory.create(it[0], it.drop(1).map(String::toInt)) }

        val theR = 1
        var registers = Registers(0, 0, 0, 0, 0, 0)
        do {
            registers = program[registers.get(5)].execute(registers)
            registers.set(ip, registers.get(ip) + 1)
        } while (registers.get(5) != 28)

        System.out.println("Answer: ${registers.get(theR)}")
    }
}

fun main() {
    Day21p1.main()
}
