package net.wojtekk.aoc2018.days.day07

import net.wojtekk.aoc2018.utils.FileHelper.readLines

data class Step2(val name: String, val prev: MutableSet<String>, val next: MutableSet<String>, var cons: Int)

val cntWorkers = 5
val delay = 60

val offset = ('A'..'Z').map { it.toString() }.zip(1..26).toMap()

fun main() {
    val path = "day07/input.txt"
    val registry = mutableMapOf<String, Step2>()
    readLines(path)
            .map { it.split(" ") }
            .forEach {
                registry.putIfAbsent(it[1], Step2(it[1], mutableSetOf(), mutableSetOf(), delay + offset[it[1]]!!))
                registry.putIfAbsent(it[7], Step2(it[7], mutableSetOf(), mutableSetOf(), delay + offset[it[7]]!!))
                registry[it[1]]!!.next.add(it[7])
                registry[it[7]]!!.prev.add(it[1])
            }

    var awaiting = registry.asSequence()
            .filter { it.value.prev.isEmpty() }
            .sortedBy { it.key }
            .map { it.value }
            .toMutableSet()

    var res = ""

    var workers = mutableListOf<Step2>()

    var counter = -1

    do {
        workers.forEach { it.cons--; if (it.cons == 0) res += it.name }
        workers = workers.filterNot { it.cons == 0 }.toMutableList()

        val nexts = awaiting.sortedBy { it.name }.filter { it.prev.all { c -> res.contains(c) } }.take(cntWorkers - workers.count())
        nexts.forEach { current ->
            awaiting.addAll(current.next.map { registry[it]!! })
            awaiting = awaiting.filterNot { it == current }.sortedBy { it.name }.toMutableSet()
            workers.add(current)
        }

        counter++
    } while (awaiting.isNotEmpty() || workers.sumBy { it.cons } != 0)

    System.out.println("Answer: $counter")
}
