package net.wojtekk.aoc2018.days.day24

object Day24p2 {

    fun main() {
        var boost = 1
        var resGroups: List<Group>

        do {
            println("Boost: $boost")
            val groups = Day24p1.readData(boost)
            resGroups = Battle().main(groups.toMutableList())
            boost++
        } while (resGroups.groupBy { it.type }.size > 1 || resGroups[0].type != GroupType.IMMUNE)

        val res = resGroups.sumBy { it.units }

        System.out.println("Answer: $res")
    }
}


fun main() {
    Day24p2.main()
}
