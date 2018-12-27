package net.wojtekk.aoc2018.days.day23

import net.wojtekk.aoc2018.utils.FileHelper.readLines


object Day23p2 {
    fun main() {
        val path = "day23/input.txt"
        var robots = readLines(path)
                .map { it.split(Regex("[^-0-9]+")).drop(1).map(String::toInt) }
                .map { Robot(Position(it[0], it[1], it[2]), it[3]) }

        var maxX = robots.map { it.position.x }.max()!!
        var minX = robots.map { it.position.x }.min()!!

        var maxY = robots.map { it.position.y }.max()!!
        var minY = robots.map { it.position.y }.min()!!

        var maxZ = robots.map { it.position.z }.max()!!
        var minZ = robots.map { it.position.z }.min()!!

        // Inspired discussion on Reddit:
        // https://www.reddit.com/r/adventofcode/comments/a8s17l/2018_day_23_solutions/ecddus1/
        // about zoom in/out strategy.

        // zoom out
        var step = 1
        while (step < listOf(maxX - minX, maxY - minY, maxZ - minZ).max()!!)
            step *= 2

        var bestPlace: Pair<Position, Int> = Pair(Position(0, 0, 0), 0)

        val p0 = Position(0, 0, 0)
        do {
            for (x in minX..maxX step step)
                for (y in minY..maxY step step)
                    for (z in minZ..maxZ step step) {
                        val p = Position(x, y, z)
                        val inRange = robots
                                .filter { distance(it.position, p) <= it.range }
                                .count()
                        if (inRange > bestPlace.second ||
                                (inRange == bestPlace.second &&
                                        distance(p0, p) < distance(p0, bestPlace.first)))
                            bestPlace = Pair(p, inRange)
                    }

            // zoom in
            minX = bestPlace.first.x - step
            maxX = bestPlace.first.x + step
            minY = bestPlace.first.y - step
            maxY = bestPlace.first.y + step
            minZ = bestPlace.first.z - step
            maxZ = bestPlace.first.z + step
            step /= 2
        } while (step > 0)

        val res = distance(p0, bestPlace.first)

        System.out.println("Answer: $res")
    }

    fun distance(p1: Position, p2: Position): Int {
        return listOf(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z)
                .map(Math::abs)
                .sum()
    }
}

fun main() {
    Day23p2.main()
}

