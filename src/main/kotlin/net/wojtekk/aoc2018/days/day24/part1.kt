package net.wojtekk.aoc2018.days.day24

import net.wojtekk.aoc2018.utils.FileHelper.readLines

enum class GroupType { IMMUNE, INFECTION }

data class Group(val type: GroupType,
                 var units: Int,
                 val hitPoints: Int,
                 val weaknesses: List<String>,
                 val immuneTo: List<String>,
                 val attackPower: Int,
                 val damage: String,
                 val initiative: Int) : Comparable<Group> {
    val id = Group.getNextId(type)

    override fun compareTo(other: Group): Int {
        if (this.getEffectivePower() == other.getEffectivePower())
            return -1 * this.initiative.compareTo(other.initiative)
        return -1 * this.getEffectivePower().compareTo(other.getEffectivePower())
    }

    fun getEffectivePower() = units * attackPower

    fun isImmuneTo(damage: String): Boolean {
        return immuneTo.contains(damage)
    }

    fun isWeakness(damage: String): Boolean {
        return weaknesses.contains(damage)
    }

    override fun toString(): String {
        return "Group(type=$type, id=$id, units=$units, hitPoints=$hitPoints weak=$weaknesses, " +
                "immune=$immuneTo damage=$damage attackPower=$attackPower initiative=$initiative " +
                "effectivePower=${this.getEffectivePower()})"
    }

    companion object {
        var ids = emptyMap<GroupType, Int>().toMutableMap()
        fun getNextId(type: GroupType): Int {
            ids.putIfAbsent(type, 0)
            ids.computeIfPresent(type) { _, v -> v + 1 }
            return ids.get(type)!!
        }
    }
}

class Battle {
    fun main(groups: MutableList<Group>): List<Group> {
        do {
            val immuneGroup = groups.filter { it.type == GroupType.IMMUNE }.toMutableList()
            val infectionGroup = groups.filter { it.type == GroupType.INFECTION }.toMutableList()
            val battles = groups.sorted()
                    .map { g ->
                        val enemies = if (g.type == GroupType.INFECTION) immuneGroup else infectionGroup
                        val e = enemies
                                .map { Pair(it, attackImpact(g, it)) }
                                .filter { it.second > 0 }
                                .sortedWith(Comparator { p1, p2 ->
                                    if (p1.second == p2.second) {
                                        if (p1.first.getEffectivePower() == p2.first.getEffectivePower())
                                            -1 * p1.first.initiative.compareTo(p2.first.initiative)
                                        else
                                            -1 * p1.first.getEffectivePower().compareTo(p2.first.getEffectivePower())
                                    } else
                                        -1 * p1.second.compareTo(p2.second)
                                })
                        val enemy = e
                                .firstOrNull()
                                .let { it?.first }
                        if (enemy != null) {
                            enemies.remove(enemy)
                            return@map Pair(g, enemy)
                        }
                        return@map null
                    }
                    .filterNotNull()

            battles
                    .sortedByDescending { it.first.initiative }
                    .forEach { (attacker, enemy) ->
                        if (!groups.contains(attacker)) return@forEach
                        val impact = attackImpact(attacker, enemy)
                        val killing = Math.min(enemy.units, Math.floor(impact.toDouble() / enemy.hitPoints.toDouble()).toInt())
                        enemy.units -= killing
                        if (enemy.units <= 0) groups.remove(enemy)
                    }
        } while (groups.groupingBy { it.type }.eachCount().keys.size > 1)

        return groups
    }

    fun attackImpact(attacker: Group, enemy: Group): Int {
        val multiplier = when {
            enemy.isWeakness(attacker.damage) -> 2
            enemy.isImmuneTo(attacker.damage) -> 0
            else -> 1
        }
        return attacker.getEffectivePower() * multiplier
    }

}

object Day24p1 {

    val separator = "Infection:"

    fun main() {
        val groups = readData()
        val resGroups = Battle().main(groups.toMutableList())

        val res = resGroups.sumBy { it.units }

        System.out.println("Answer: $res")
    }

    fun readData(immuneSystemBoost: Int = 0): List<Group> {
        val path = "day24/input.txt"
        val input = readLines(path)

        val immuneSystem = input
                .drop(1)
                .takeWhile { it != separator }
                .map(this::parse)
                .map { createGroup(GroupType.IMMUNE, it, immuneSystemBoost) }

        val infections = input
                .reversed()
                .takeWhile { it != separator }
                .reversed()
                .map(this::parse)
                .map { createGroup(GroupType.INFECTION, it) }

        val groups = mutableListOf<Group>()
        groups.addAll(immuneSystem)
        groups.addAll(infections)

        return groups
    }

    fun parse(line: String): List<String> {
        return ("([0-9]+) units each with ([0-9]+) hit points([ ]*| \\(([^)]+)\\) )with " +
                "an attack that does ([0-9]+) ([a-z]+) damage at initiative ([0-9]+)")
                .toRegex()
                .find(line)
                .let { it!!.groupValues.drop(1).map(String::trim) }
                .let { listOf(it[0], it[1], it[3], it[4], it[5], it[6]) }
    }

    fun createGroup(type: GroupType, data: List<String>, boost: Int = 0): Group {
        val weakAndImmune = data[2].split(";")
                .map { it.split(" to ") }
                .filter { it.size == 2 }
                .map { it.map(String::trim) }
                .map { it[0] to it[1].split(",").map(String::trim) }
                .toMap()

        return Group(type,
                data[0].toInt(),
                data[1].toInt(),
                weakAndImmune.getOrDefault("weak", emptyList()),
                weakAndImmune.getOrDefault("immune", emptyList()),
                data[3].toInt() + boost,
                data[4],
                data[5].toInt())
    }
}

fun main() {
    Day24p1.main()
}
