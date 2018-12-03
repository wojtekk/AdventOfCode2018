package net.wojtekk.aoc2018.days.day01

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.util.*
import kotlin.collections.ArrayList


fun main() {
    val path = "day01/input.txt"
    val registry: ArrayList<Int> = ArrayList()
    val numbers = readLines(path).map { Integer.valueOf(it, 10) }
    var res: Optional<Int> = Optional.empty()
    var current = 0
    while (res.isEmpty) {
        loop@ for (i in numbers) {
            registry.add(current)
            current += i
            if (registry.contains(current)) {
                res = Optional.of(current)
                break@loop
            }
        }
    }
    System.out.println("Answer: $res")
}
