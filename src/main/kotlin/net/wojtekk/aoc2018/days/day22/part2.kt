package net.wojtekk.aoc2018.days.day22

import java.util.*

enum class RegionsType {
    ROCKY {
        override fun allowedTool(tool: Tool) = tool != Tool.NEITHER
    },
    WET {
        override fun allowedTool(tool: Tool) = tool != Tool.TORCH
    },
    NARROW {
        override fun allowedTool(tool: Tool) = tool != Tool.CLIMBING_GEAR
    };

    abstract fun allowedTool(tool: Tool): Boolean

    companion object {
        fun fromCode(code: Int) = values()[code]
    }
}

enum class Tool {
    NEITHER,
    CLIMBING_GEAR,
    TORCH;
}

data class Step(val x: Int, val y: Int, val tool: Tool, val distance: Int) : Comparable<Step> {
    override fun compareTo(other: Step): Int {
        return this.distance.compareTo(other.distance)
    }
}

data class Point(var x: Int, var y: Int)

object Day22p2 {
    val depth = 11394
    val target = Point(7, 701)

    val width = 200
    val height = 2000

    val GI = 20183
    val EI = 3
    val GIY = 16807
    val GIX = 48271
    val map = Array(height) { Array(width) { RegionsType.NARROW } }
    val elMap = Array(height) { IntArray(width) { 0 } }

    fun main() {
        for (y in 0 until height)
            for (x in 0 until width)
                elMap[y][x] = erosionLevel(x, y)

        for (y in 0 until height)
            for (x in 0 until width)
                map[y][x] = regionType(elMap[y][x])

        // Inspiration: https://www.reddit.com/r/adventofcode/comments/a8i1cy/2018_day_22_solutions/ecaxbe7/
        // and Dijkstra’s shortest path algorithm: https://www.youtube.com/watch?v=_lHSawdgXpI

        val visited = Array(height) {
            Array(width) { Tool.values().map { t -> t to false }.toMap().toMutableMap() }
        }
        val distance = Array(height) {
            Array(width) { Tool.values().map { t -> t to Int.MAX_VALUE }.toMap().toMutableMap() }
        }

        val queue = PriorityQueue<Step>()
        queue.add(Step(0, 0, Tool.TORCH, 0))

        do {
            val current = queue.remove()

            if (visited[current.y][current.x][current.tool]!!) continue

            visited[current.y][current.x][current.tool] = true
            distance[current.y][current.x][current.tool] = current.distance

            for (p in getNeighbours(current.x, current.y)) {
                val s = Step(p.x, p.y, current.tool, current.distance + 1)
                if (map[s.y][s.x].allowedTool(s.tool) && !visited[s.y][s.x][s.tool]!!) queue.add(s)
            }

            for (t in Tool.values()) {
                val s = Step(current.x, current.y, t, current.distance + 7)
                if (map[s.y][s.x].allowedTool(s.tool) && !visited[s.y][s.x][s.tool]!!) queue.add(s)
            }
        } while (queue.isNotEmpty())

        System.out.println("Answer: ${distance[target.y][target.x][Tool.TORCH]}")
    }

    fun getNeighbours(x: Int, y: Int): List<Point> {
        return listOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1))
                .filter { it.x >= 0 && it.y >= 0 }
                .filter { it.x < width && it.y < height }
    }

    fun erosionLevel(x: Int, y: Int): Int {
        if (x == 0 && y == 0) return depth.rem(GI)
        else if (x == target.x && y == target.y) return depth.rem(GI)
        else if (y == 0) return (x * GIY + depth).rem(GI)
        else if (x == 0) return (y * GIX + depth).rem(GI)
        return ((elMap[y][x - 1].rem(GI) * elMap[y - 1][x].rem(GI)).rem(GI) + depth.rem(GI)).rem(GI)
    }

    fun regionType(erosionLevel: Int) = RegionsType.fromCode(erosionLevel.rem(EI))
}

fun main() {
    Day22p2.main()
}
