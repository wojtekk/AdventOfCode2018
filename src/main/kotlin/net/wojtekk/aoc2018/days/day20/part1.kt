package net.wojtekk.aoc2018.days.day20

import net.wojtekk.aoc2018.utils.FileHelper.readLines

class Node(var key: String, val parent: Node?, val childrens: MutableList<Node> = mutableListOf())

object Day20p1 {
    fun main() {
        val path = "day20/input.txt"
        val map = readLines(path).first()

        val tree = buildTree(map)
        val res = findPath("", tree.childrens.first())

        System.out.println("Answer: ${res.length} +1 for my input - I don't know where I've lost one dor :/")
    }

    fun buildTree(map: String): Node {
        val startNode = Node(map.first().toString(), null)
        var node = Node("", startNode)
        startNode.childrens.add(node)

        for (c in map.drop(1).dropLast(1)) {
            when (c) {
                '(' -> {
                    node = Node("", node)
                    node.parent!!.childrens.add(node)
                }
                ')' -> {
                    if (node.key.isEmpty()) {
                        node.parent!!.childrens.clear()
                        node = node.parent!!
                    } else {
                        node = node.parent!!
                    }
                }
                '|' -> {
                    node = Node("", node.parent)
                    node.parent!!.childrens.add(node)
                }
                else -> node.key += c
            }
        }
        return startNode
    }

    fun findPath(path: String, map: Node): String {
        val newPath = "$path${map.key}"
        if (map.childrens.size == 0) return newPath

        val paths = mutableListOf<String>()
        for (p in map.childrens) {
            paths.add(findPath(newPath, p))
        }

        return paths.maxBy { it.length }!!
    }
}

fun main() {
    Day20p1.main()
}
