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
    (0 until gridSize - 2).forEach { x ->
        (0 until gridSize - 2).forEach { y ->
            squares += Pair("$x,$y", (0..2).flatMap { i -> (0..2).map { j -> grid[x + i][y + j];  } }.sum())
        }
    }

    val res = squares.maxBy { it.second }

    System.out.println("Answer: $res ")
}
