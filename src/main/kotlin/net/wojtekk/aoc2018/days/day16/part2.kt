package net.wojtekk.aoc2018.days.day16

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.util.*

object Day16p2 {
    fun main() {
        val path = "day16/input.txt"
        val instructionCandidates = readLines(path)
                .asSequence()
                .windowed(3, 3, false) { it.joinToString("") }
                .filter { it.contains("Before") }
                .map { it.split(Regex("[^0-9]+")).drop(1).dropLast(1).map(String::toInt).chunked(4) }
                .flatMap(::validate)
                .groupingBy { it.second }.aggregate { _, accumulator: MutableSet<String>?, element, first ->
                    if (first) {
                        mutableSetOf(element.first)
                    } else {
                        accumulator!!.add(element.first)
                        accumulator
                    }
                }
                .map { Pair(it.key, it.value!!.toSet()) }
                .toList()

        val opcodeToInstruction = matchInstructions(mapOf(), instructionCandidates)
                .get()
                .map { Pair(it.value, it.key) }.toMap()

        val program = readLines(path)
                .chunked(3)
                .filterNot { it[0].contains("Before") }
                .flatten()
                .map { it.split(Regex("[ ]+")).map(String::toInt) }
                .map { InstructionFactory.create(opcodeToInstruction[it[0]]!!, it) }

        var res = Registry(0, 0, 0, 0)
        program.forEach { res = it.execute(res) }

        System.out.println("Answer: $res")
    }

    private fun matchInstructions(acc: Map<String, Int>, list: List<Pair<Int, Set<String>>>): Optional<Map<String, Int>> {
        if (list.isEmpty()) return Optional.of(acc)
        val opcode = list[0].first
        val instructions = list[0].second
        val restList = list.subList(1, list.size)
        for (instruction in instructions) {
            if (acc.containsKey(instruction)) continue
            val res = matchInstructions(acc + mapOf(instruction to opcode), restList)
            if (res.isPresent) return res
        }

        return Optional.empty()
    }

    private fun validate(list: List<List<Int>>): Sequence<Pair<String, Int>> {
        val before = Registry(list[0][0], list[0][1], list[0][2], list[0][3])
        val data = list[1]
        val after = Registry(list[2][0], list[2][1], list[2][2], list[2][3])

        return InstructionFactory.getAll()
                .map { InstructionFactory.create(it, data) }
                .filter { it.execute(before) == after }
                .map { Pair(it.name, data[0]) }
                .asSequence()
    }
}

fun main() {
    Day16p2.main()
}
