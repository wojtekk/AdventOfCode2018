package net.wojtekk.aoc2018.days.day14

fun main() {
    val input = 920831

    val list = LinkedList()

    var buf = "37"

    list.add(LinkedList.Node(3))
    list.add(LinkedList.Node(7))

    list.init("a")
    list.init("b")
    list.move("b", 1)

    do {
        val value = list.get("a") + list.get("b")
        if (value >= 10) {
            list.add(LinkedList.Node(value.div(10)))
            buf += value.div(10).toString()
            if (buf.length > 6)
                buf = buf.substring(buf.length - 6, buf.length)
                if(buf == input.toString()) {
                    val res = list.size - input.toString().length
                    System.out.println("Answer: $res")
                    System.exit(0)
                }
        }
        list.add(LinkedList.Node(value.rem(10)))
        buf += value.rem(10).toString()

        if (buf.length > 6)
            buf = buf.substring(buf.length - 6, buf.length)

        list.move("a", 1 + list.get("a"))
        list.move("b", 1 + list.get("b"))
        if(list.size.rem(1000000) == 0) System.out.println("${list.size} $buf")
    } while (buf != input.toString())

    val res = list.size - input.toString().length

    System.out.println("Answer: $res")
}
