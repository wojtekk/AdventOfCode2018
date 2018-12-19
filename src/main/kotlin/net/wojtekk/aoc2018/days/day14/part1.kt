package net.wojtekk.aoc2018.days.day14

class LinkedList {
    var first: Node? = null
    var last: Node? = null
    val pointers: MutableMap<String, Node> = mutableMapOf()
    val positions: MutableMap<String, Int> = mutableMapOf()
    var size = 0

    fun init(pointer: String) {
        pointers.putIfAbsent(pointer, first!!)
        positions.putIfAbsent(pointer, 0)
    }

    fun move(pointer: String, steps: Int) {
        var element = pointers[pointer]!!
        (0 until steps).forEach {
            element = element.next!!
            positions[pointer] = (size + positions[pointer]!! + 1).rem(size)
        }
        pointers[pointer] = element
    }

    fun add(element: Node) {
        if (first == null && last == null) {
            element.next = element
            first = element
            last = element
        } else {
            last?.next = element
            element.next = first
            last = element
        }
        size++
    }

    fun get(pointer: String): Int {
        return pointers[pointer]?.value!!
    }

    fun getScore(x: Int): String {
        var res = ""
        init("c")
        move("c", x - 1)
        (0 until 10).forEach { move("c", 1); res += get("c") }
        return res
    }

    class Node(val value: Int, var next: Node? = null)
}

fun main() {
    val input = 920831

    val list = LinkedList()

    list.add(LinkedList.Node(3))
    list.add(LinkedList.Node(7))

    list.init("a")
    list.init("b")
    list.move("b", 1)

    do {
        val value = list.get("a") + list.get("b")
        if (value >= 10) list.add(LinkedList.Node(value.div(10)))
        list.add(LinkedList.Node(value.rem(10)))
        list.move("a", 1 + list.get("a"))
        list.move("b", 1 + list.get("b"))
    } while (list.size - 1 < input + 10)

    val res = list.getScore(input)

    System.out.println("Answer: $res")
}
