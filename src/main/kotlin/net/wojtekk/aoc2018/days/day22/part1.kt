package net.wojtekk.aoc2018.days.day22

object Day22p1 {
    val depth = 11394
    val target = Pair(7, 701)
    val GI = 20183
    val EI = 3
    val GIY = 16807
    val GIX = 48271
    val map = Array(target.second + 1) { IntArray(target.first + 1) { 0 } }
    val elMap = Array(target.second + 1) { IntArray(target.first + 1) { 0 } }

    fun main() {
        for (y in 0..target.second)
            for (x in 0..target.first) {
                elMap[y][x] = erosionLevel(x, y)
                map[y][x] = elMap[y][x].rem(EI)
            }

        val res = map.map { it.sum() }.sum()
        System.out.println("Answer: $res")
    }

    fun erosionLevel(x: Int, y: Int): Int {
        if (x == 0 && y == 0) return depth.rem(GI)
        else if (x == target.first && y == target.second) return depth.rem(GI)
        else if (y == 0) return (x * GIY + depth).rem(GI)
        else if (x == 0) return (y * GIX + depth).rem(GI)
        return ((elMap[y][x - 1].rem(GI) * elMap[y - 1][x].rem(GI)).rem(GI) + depth.rem(GI)).rem(GI)
    }
}

fun main() {
    Day22p1.main()
}
