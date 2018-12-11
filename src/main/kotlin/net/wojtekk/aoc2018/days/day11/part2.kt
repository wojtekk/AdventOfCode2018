package net.wojtekk.aoc2018.days.day11

fun main() {
    val serial = 2568
    val gridSize = 300
    val grid = Array(gridSize) { IntArray(gridSize) { 0 } }
    (0 until gridSize).forEach { x ->
        (0 until gridSize).forEach { y ->
            val rackId = x + 10
            grid[x][y] = (((rackId * y) + serial) * rackId).toString().reversed().getOrElse(2) { '0' }.toString().toInt() - 5
        }
    }

    val squares = mutableListOf<Pair<String, Int>>()
    (0 until gridSize).forEach { x ->
        (0 until gridSize).forEach { y ->
            var power = 0
            (0 until gridSize).forEach { squareSize ->
                if (x + squareSize < gridSize && y + squareSize < gridSize) {
                    power += (0 .. squareSize).map { grid[x + it][y + squareSize] }.sum()
                    power += (0 until squareSize).map { grid[x + squareSize][y + it] }.sum()
                    squares += Pair("$x,$y,${squareSize + 1}", power)
                }
            }
        }
    }

    val res = squares.maxBy { it.second }

    System.out.println("Answer: $res ")
}
