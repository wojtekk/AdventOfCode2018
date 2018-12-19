package net.wojtekk.aoc2018.days.day13

import java.io.File
import java.lang.StringBuilder
import java.util.*

enum class Direction(var value: Char) {
    TOP('^'),
    RIGHT('>'),
    DOWN('v'),
    LEFT('<');

    fun turn(t: Turn): Direction {
        return values()[(values().size + this.ordinal + t.getValue()).rem(values().size)]
    }

    companion object {
        fun fromChar(c: Char): Direction {
            return values().first { it.value == c }
        }
    }
}

enum class Turn {
    LEFT {
        override fun getValue() = -1
    },
    STRAIGHT {
        override fun getValue() = 0
    },
    RIGHT {
        override fun getValue() = 1
    };

    abstract fun getValue(): Int

    fun next() = when (this) {
        LEFT -> STRAIGHT
        STRAIGHT -> RIGHT
        RIGHT -> LEFT
    }
}

data class Car(var x: Int, var y: Int, var direction: Direction, var nextTurn: Turn) {
    fun drive() {
        when (this.direction) {
            Direction.TOP -> y--
            Direction.RIGHT -> x++
            Direction.DOWN -> y++
            Direction.LEFT -> x--
        }
    }

    fun navigate(roads: List<String>) {
        val position = roads[y][x]
        if (position == '+') {
            direction = direction.turn(nextTurn)
            nextTurn = nextTurn.next()
        } else if (position == '/' && (direction == Direction.DOWN || direction == Direction.TOP)) {
            direction = direction.turn(Turn.RIGHT)
        } else if (position == '/' && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
            direction = direction.turn(Turn.LEFT)
        } else if (position == '\\' && (direction == Direction.DOWN || direction == Direction.TOP)) {
            direction = direction.turn(Turn.LEFT)
        } else if (position == '\\' && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
            direction = direction.turn(Turn.RIGHT)
        } else if (position != '|' && position != '-') {
            throw Error("Out of road :(")
        }
    }
}

val carsCharacters = "v^<>"

fun main() {
    val path = "day13/input.txt"
    val initialState = File(ClassLoader.getSystemResource(path).file)
            .readLines()
            .filter(String::isNotEmpty)

    val maxLength = initialState.map { it.length }.max()!!
    val roadsWithCars = initialState.map { it.padEnd(maxLength, ' ') }

    val cars = findCars(roadsWithCars)

    val roads = removeCars(roadsWithCars)

    var collision: Optional<Pair<Int, Int>> = Optional.empty()
    do {
        for (car in cars) {
            car.drive()
            car.navigate(roads)
            collision = detectCollitions(cars)
            if (collision.isPresent) break
        }
    } while (collision.isEmpty)

    System.out.println("Answer: $collision")
}


fun findCars(roads: List<String>): MutableList<Car> {
    val cars = mutableListOf<Car>()
    roads.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (carsCharacters.contains(c)) cars.add(Car(x, y, Direction.fromChar(roads[y][x]), Turn.LEFT))
        }
    }

    return cars
}

fun hasNeighbours(roads: List<String>, x: Int, y: Int): String {
    return StringBuilder()
            .append(roads.getOrNull(y - 1)?.getOrNull(x) ?: ' ')
            .append(roads.getOrNull(y)?.getOrNull(x + 1) ?: ' ')
            .append(roads.getOrNull(y + 1)?.getOrNull(x) ?: ' ')
            .append(roads.getOrNull(y)?.getOrNull(x - 1) ?: ' ')
            .toString()
}

fun removeCars(roads: List<String>): List<String> {
    val repairedRoads = mutableListOf<String>()
    roads.forEachIndexed { y, line ->
        repairedRoads.add("")
        line.forEachIndexed { x, c ->
            repairedRoads[y] = repairedRoads[y] + if (carsCharacters.contains(c)) {
                val neighbours = hasNeighbours(roads, x, y)
                when {
                    Regex("|-|-").matches(neighbours) -> '+'
                    Regex("\\+\\+\\+\\+").matches(neighbours) -> '+'
                    Regex(".[-+\\\\/].[-+\\\\/]").matches(neighbours) -> '-'
                    Regex("[|+\\\\/].[|+\\\\/].").matches(neighbours) -> '|'
                    Regex("-|.").matches(neighbours) -> '/'
                    Regex("..|-").matches(neighbours) -> '\\'
                    Regex("|..-").matches(neighbours) -> '/'
                    Regex("|-..").matches(neighbours) -> '\\'
                    else -> throw Error("Can't remove car: $c, $x, $y, [$neighbours]")
                }
            } else {
                roads[y][x]
            }
        }
    }

    return repairedRoads.toList()
}

fun detectCollitions(cars: List<Car>): Optional<Pair<Int, Int>> {
    val position = cars.groupingBy { Pair(it.x, it.y) }
            .eachCount()
            .filter { it.value > 1 }
            .maxBy { it.value }
            .let { it?.key }
    return Optional.ofNullable(position)
}
