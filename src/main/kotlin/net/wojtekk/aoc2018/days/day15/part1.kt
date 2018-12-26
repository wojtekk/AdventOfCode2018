package net.wojtekk.aoc2018.days.day15

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.util.*

// http://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html#8-colors
// https://unix.stackexchange.com/questions/138804/how-to-transform-a-text-file-into-a-picture
const val ESC = "\u001B"
const val NORMAL = ESC + "[0m"
const val REVERSED = ESC + "[7m"
const val RED = ESC + "[31m"
const val GREEN = ESC + "[32m"
const val YELLOW = ESC + "[33m"
const val BLUE = ESC + "[34m"

open class Position(val x: Int, val y: Int) : Comparable<Position> {
    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

    override fun compareTo(other: Position): Int {
        if (this.y == other.y)
            return this.x.compareTo(other.x)
        return this.y.compareTo(other.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}

class ReachablePosition(x: Int, y: Int, val distance: Int) : Position(x, y) {
    constructor(position: Position, distance: Int) : this(position.x, position.y, distance)

    override fun compareTo(other: Position): Int {
        if (other is ReachablePosition) {
            return this.compareTo(other)
        }
        return super.compareTo(other)

    }

    fun compareTo(other: ReachablePosition): Int {
        if (this.distance == other.distance) {
            if (this.y == other.y)
                return this.x.compareTo(other.x)
            return this.y.compareTo(other.y)
        }
        return this.distance.compareTo(other.distance)
    }

    override fun toString(): String {
        return "ReachablePosition(x=$x, y=$y, distance=$distance)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ReachablePosition

        if (distance != other.distance) return false

        return true
    }
}

abstract class Unit(var position: Position, var hp: Int = 200, var attackPower: Int = 3) : Comparable<Unit> {
    abstract val type: String
    abstract val code: Char

    override fun compareTo(other: Unit): Int {
        if (this.hp == other.hp) {
            return this.position.compareTo(other.position)
        }
        return this.hp.compareTo(other.hp)
    }

    companion object {
        fun fromChar(c: Char, x: Int, y: Int) =
                when (c) {
                    'G' -> Goblin(Position(x, y))
                    'E' -> Elf(Position(x, y))
                    else -> null
                }

        fun code() = Unit::code
    }

    override fun toString(): String {
        return "Unit(type='$type', position=(${position.x}, ${position.y}), hp=$hp, attackPower=$attackPower)"
    }
}

class Goblin(position: Position) : Unit(position) {
    override val type = "goblin"
    override val code = 'G'
}

class Elf(position: Position) : Unit(position) {
    override val type = "elf"
    override val code = 'E'
}

enum class Action { NONE, MOVE, ATTACK }

class Combat(val units: MutableList<Unit>, val map: List<BooleanArray>) {
    val mapWidth = map[0].size
    val mapHeight = map.size
    val neighbour = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    // false = unit or wall, true = available
    val mapOfUnits = List(mapHeight) { MutableList<Unit?>(mapWidth) { null } }

    fun fight(debug: Boolean = false): Triple<String, Int, Int> {
        updateMapOfUnits()

        var round = 0
        var done = false
        if (debug) {
            println(round)
            print()
        }

        while (true) {
            if (units.groupingBy { it.type }.eachCount().keys.size == 1) break
            val sortedUnits = units.sortedBy { it.position }
            for (unit in sortedUnits) {
                if (!units.contains(unit)) continue
                when (nextAction(unit)) {
                    Action.MOVE -> {
                        actionMove(unit)
                        if (nextAction(unit) == Action.ATTACK)
                            actionAttack(unit)
                    }
                    Action.ATTACK -> actionAttack(unit)
                    Action.NONE -> done = true
                }
                if (done) break
            }
            if (done) break

            round++

            if (debug) {
                println(round)
                print()
                units.forEach(::println)
                println()
            }
        }

        return Triple(units.first().type, units.count { it.type == "elf" }, units.map { it.hp }.sum() * round)
    }

    fun actionAttack(unit: Unit) {
        val enemy = choseEnemyToAttack(unit)
        doAttackUnit(unit, enemy)
    }

    fun doAttackUnit(unit: Unit, enemy: Unit) {
        enemy.hp -= unit.attackPower
        if (enemy.hp <= 0) {
            mapOfUnits[enemy.position.y][enemy.position.x] = null
            units.remove(enemy)
        }
    }

    fun actionMove(unit: Unit) {
        val targets = findTargets(unit)
        val inRange = findInRange(targets)
        val reachable = findReachable(unit, inRange)
        if (reachable.isEmpty()) return
        val chosen = chosePosition(reachable)
        val destination = calculateNextStep(unit.position, chosen)
//        println("$unit -> $chosen -> $destination <- $reachable")
        doMoveUnit(unit, destination)

//        print(unit, reachable)
    }

    fun getAdjacentEnemies(unit: Unit): List<Unit> {
        return neighbour
                .map { Position(unit.position.x + it.first, unit.position.y + it.second) }
                .mapNotNull { mapOfUnits[it.y][it.x] }
                .filter { it.type != unit.type }
    }

    fun choseEnemyToAttack(unit: Unit): Unit {
        return getAdjacentEnemies(unit)
                .sorted()
                .first()
    }

    fun nextAction(unit: Unit): Action {
        val enemies = getAdjacentEnemies(unit)
        return if (enemies.isNotEmpty()) Action.ATTACK
        else if (units.any { unit.type != it.type }) Action.MOVE
        else Action.NONE
    }

    fun doMoveUnit(unit: Unit, destination: Position) {
        mapOfUnits[unit.position.y][unit.position.x] = null
        mapOfUnits[destination.y][destination.x] = unit
        unit.position = Position(destination.x, destination.y)
    }

    fun chosePosition(positions: List<ReachablePosition>): ReachablePosition {
        return positions.sorted().first()
    }

    fun findReachable(unit: Unit, positions: List<Position>): List<ReachablePosition> {
        val distances = calculateDistance(unit.position)

        return positions.map { ReachablePosition(it, distances[it.y][it.x]) }
                .filterNot { it.distance == Int.MAX_VALUE }
                .sorted()
    }

    fun calculateNextStep(start: Position, destination: Position): Position {
        val distances = calculateDistance(destination)

        return neighbour.map { Position(start.x + it.first, start.y + it.second) }
                .map { ReachablePosition(it, distances[it.y][it.x]) }
                .sorted()
                .first()
    }

    private fun calculateDistance(position: Position): Array<IntArray> {
        val visited = Array(mapHeight) { BooleanArray(mapWidth) { false } }
        val distances = Array(mapHeight) { IntArray(mapWidth) { Int.MAX_VALUE } }

        val queue = PriorityQueue<ReachablePosition>()
        queue.add(ReachablePosition(position, 0))

        do {
            val current = queue.remove()

            if (visited[current.y][current.x]) continue

            visited[current.y][current.x] = true
            distances[current.y][current.x] = current.distance

            for (p in getReachableNeighbours(current.x, current.y)) {
                val s = ReachablePosition(p, current.distance + 1)
                if (!visited[p.y][p.x]) queue.add(s)
            }
        } while (queue.isNotEmpty())
        return distances
    }

    fun getReachableNeighbours(x: Int, y: Int): List<Position> {
        return neighbour
                .map { Position(x + it.first, y + it.second) }
                .filter { it.x >= 0 && it.y >= 0 }
                .filter { it.x < mapWidth && it.y < mapHeight }
                .filter { map[it.y][it.x] }
                .filter { mapOfUnits[it.y][it.x] == null }
    }

    fun updateMapOfUnits() {
        units.forEach { mapOfUnits[it.position.y][it.position.x] = it }
    }

    fun findTargets(unit: Unit): List<Unit> {
        return units.filter { unit.type != it.type }
    }

    fun findInRange(enemies: List<Unit>): List<Position> {
        return enemies
                .map { it.position }
                .flatMap { neighbour.map { n -> Position(it.x + n.first, it.y + n.second) } }
                .filter { it.y >= 0 && it.x >= 0 }
                .filter { it.y < mapHeight && it.x < mapWidth }
                .filter { map[it.y][it.x] }
                .filter { mapOfUnits[it.y][it.x] == null }
    }


    fun print(unit: Unit? = null,
              reachable: List<ReachablePosition> = emptyList(),
              nearest: List<ReachablePosition> = emptyList(),
              chosen: ReachablePosition? = null) {
        map.forEachIndexed { y, l ->
            l.forEachIndexed { x, _ ->
                val rp = reachable.firstOrNull { it.x == x && it.y == y }
                val c = if (unit != null && unit.position.x == x && unit.position.y == y) {
                    if (mapOfUnits[y][x]!!.code == 'E')
                        "$REVERSED$BLUE${mapOfUnits[y][x]!!.code}"
                    else
                        "$REVERSED$RED${mapOfUnits[y][x]!!.code}"
                } else if (mapOfUnits[y][x] != null) {
                    if (mapOfUnits[y][x]!!.code == 'E')
                        "$BLUE${mapOfUnits[y][x]!!.code}"
                    else
                        "$RED${mapOfUnits[y][x]!!.code}"
                } else if (rp != null) {
                    if (rp.distance > 9)
                        "$YELLOW@"
                    else
                        "$YELLOW${rp.distance.toString()[0]}"
                } else if (map[y][x])
                    "$NORMAL."
                else
                    "$NORMAL#"
                print(c)
            }
            println()
        }
        println()
    }
}

object Day15p1 {
    fun main() {
        val path = "day15/input.txt"
        val input = readLines(path)

        val units = input
                .mapIndexed { y, s -> s.mapIndexed { x, c -> Unit.fromChar(c, x, y) } }
                .flatten()
                .filterNotNull()

        val map = input
                .map { it.replace("G", ".").replace("E", ".") }
                .map { it.map { c -> c == '.' }.toBooleanArray() }

        println("Units")
        units.forEach(::println)
        println()

        println("Map")
        map.forEach { it.forEach { c -> print(if (c) "." else "#") }; println() }
        println()

        val combat = Combat(units.toMutableList(), map)

        val result = combat.fight(true)

        System.out.println("Answer: ${result.first} wins, outcome: ${result.second}")
    }
}


fun main() {
    Day15p1.main()
}
