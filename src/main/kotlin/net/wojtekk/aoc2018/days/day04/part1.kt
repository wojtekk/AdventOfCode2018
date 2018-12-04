package net.wojtekk.aoc2018.days.day04

import net.wojtekk.aoc2018.utils.FileHelper.readLines

data class Log(val date: Int, val hour: Int, val minute: Int, val action: String)

fun main() {
    val path = "day04/input.txt";
    val res = readLines(path)
        .map { it.split(Regex("[^0-9a-zA-Z]+")) }
        .map { Log("${it[1]}${it[2]}${it[3]}".toInt(), it[4].toInt(), it[5].toInt(), "${it[6]} ${it[7]}") }
        .sortedWith(compareBy<Log> { it.date }.thenBy { it.hour }.thenBy { it.minute })
        .fold(Pair<String, MutableMap<String, MutableList<Log>>>("", mutableMapOf())) { acc, it ->
            if (it.action.startsWith("Guard")) {
                Pair(it.action, acc.second)
            } else {
                acc.second.computeIfAbsent(acc.first) { mutableListOf() }
                acc.second.compute(acc.first) { _, value -> value!!.add(it); value }
                acc
            }
        }
        .let { it.second }
        .mapValues {
            it.value.withIndex()
                .groupBy { a -> a.index / 2 }
                .map { b -> b.value.map { c -> c.value } }
                .map { d -> Pair(d[0], d[1]) }
        }
        .mapValues {
            it.value.fold(IntArray(60) { 0 }) { acc, p ->
                (p.first.minute until p.second.minute).forEach { i ->
                    acc[i] += 1
                }
                acc
            }
        }
        .asSequence()
        .sortedByDescending { it.value.sum() }
        .first()
        .let { it.key.split(" ")[1].toInt() * it.value.indexOf(it.value.max()!!) }

    System.out.println("Answer: $res")
}
