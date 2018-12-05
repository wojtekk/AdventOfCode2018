package net.wojtekk.aoc2018.days.day05

import net.wojtekk.aoc2018.utils.FileHelper.readLines

fun main() {
    val path = "day05/input.txt";
    val polymer = readLines(path).joinToString("").trim()

    var res = fullReaction(polymer)

    System.out.println("Answer: ${res.length}")

}

fun fullReaction(polymer: String): String {
    var res = polymer
    var i = 0
    do {
        if (interact(res[i], res[i + 1])) {
            res = res.removeRange(i, i + 2)
            if (i > 0) {
                i--
            }
        } else {
            i++
        }
    } while (i != res.length - 1)
    return res
}

fun interact(a: Char, b: Char): Boolean {
    return a.toLowerCase() == b.toLowerCase() && (a.isUpperCase() && b.isLowerCase() || a.isLowerCase() && b.isUpperCase())
}
