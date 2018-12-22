package net.wojtekk.aoc2018.days.day19

import net.wojtekk.aoc2018.utils.FileHelper.readLines


data class Registers(var a: Int, var b: Int, var c: Int, var d: Int, var e: Int, var f: Int) {
    fun get(n: Int): Int = when (n) {
        0 -> a
        1 -> b
        2 -> c
        3 -> d
        4 -> e
        5 -> f
        else -> -1
    }

    fun set(n: Int, value: Int): Unit = when (n) {
        0 -> a = value
        1 -> b = value
        2 -> c = value
        3 -> d = value
        4 -> e = value
        5 -> f = value
        else -> throw Exception("Incorrect registry $n")
    }
}

interface InstructionExecutor {
    val name: String
    fun execute(registers: Registers): Registers
}

abstract class AbstractInstruction(val regA: Int, val regB: Int, val regC: Int) : InstructionExecutor {
    protected abstract fun apply(a: Int, b: Int): Int

    fun result(registers: Registers, value: Int): Registers {
        val res = registers.copy()
        res.set(regC, value)
        return res
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}($regA, $regB, $regC)"
    }
}

abstract class AbstractInstructionRR(regA: Int, regB: Int, regC: Int) : AbstractInstruction(regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(registers.get(regA), registers.get(regB))
        return result(registers, res)
    }
}

abstract class AbstractInstructionRI(regA: Int, regB: Int, regC: Int) : AbstractInstruction(regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(registers.get(regA), regB)
        return result(registers, res)
    }
}

abstract class AbstractInstructionIR(regA: Int, regB: Int, regC: Int) : AbstractInstruction(regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(regA, registers.get(regB))
        return result(registers, res)
    }
}


class Addr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "addr"
    override fun apply(a: Int, b: Int): Int = a + b
}

class Addi(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "addi"
    override fun apply(a: Int, b: Int): Int = a + b
}

class Mulr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "mulr"
    override fun apply(a: Int, b: Int): Int = a * b
}

class Muli(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "muli"
    override fun apply(a: Int, b: Int): Int = a * b
}

class Banr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "banr"
    override fun apply(a: Int, b: Int): Int = a and b
}

class Bani(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "bani"
    override fun apply(a: Int, b: Int): Int = a and b
}

class Borr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "borr"
    override fun apply(a: Int, b: Int): Int = a or b
}

class Bori(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "bori"
    override fun apply(a: Int, b: Int): Int = a or b
}

class Setr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "setr"
    override fun apply(a: Int, b: Int): Int = a
}

class Seti(regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(regA, regB, regC) {
    override val name = "seti"
    override fun apply(a: Int, b: Int): Int = a
}

class Gtir(regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(regA, regB, regC) {
    override val name = "gtir"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Gtri(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "gtri"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Gtrr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "gtrr"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Eqir(regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(regA, regB, regC) {
    override val name = "eqir"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

class Eqri(regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(regA, regB, regC) {
    override val name = "eqri"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

class Eqrr(regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(regA, regB, regC) {
    override val name = "eqrr"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

object InstructionFactory {
    fun create(name: String, args: List<Int>): InstructionExecutor = when (name) {
        "addr" -> Addr(args[0], args[1], args[2])
        "addi" -> Addi(args[0], args[1], args[2])
        "mulr" -> Mulr(args[0], args[1], args[2])
        "muli" -> Muli(args[0], args[1], args[2])
        "banr" -> Banr(args[0], args[1], args[2])
        "bani" -> Bani(args[0], args[1], args[2])
        "borr" -> Borr(args[0], args[1], args[2])
        "bori" -> Bori(args[0], args[1], args[2])
        "setr" -> Setr(args[0], args[1], args[2])
        "seti" -> Seti(args[0], args[1], args[2])
        "gtrr" -> Gtrr(args[0], args[1], args[2])
        "gtir" -> Gtir(args[0], args[1], args[2])
        "gtri" -> Gtri(args[0], args[1], args[2])
        "eqrr" -> Eqrr(args[0], args[1], args[2])
        "eqir" -> Eqir(args[0], args[1], args[2])
        "eqri" -> Eqri(args[0], args[1], args[2])
        else -> throw IllegalArgumentException("Unknown instruction $name")
    }
}

object Day19p1 {
    fun main() {
        val path = "day19/input.txt"
        var input = readLines(path)

        val ip = input
                .take(1)
                .map { it.replace("#ip ", "") }
                .map(String::toInt).first()

        val program = input
                .drop(1)
                .map { it.split(Regex("[ ]+")) }
                .map { InstructionFactory.create(it[0], it.drop(1).map(String::toInt)) }

        var registers = Registers(0, 0, 0, 0, 0, 0)

        var p = 0
        do {
            registers = program[p].execute(registers)
            registers.set(ip, registers.get(ip) + 1)
            p = registers.get(ip)
        } while (p < program.size)

        System.out.println("Answer: ${registers.a}")
    }
}

fun main() {
    Day19p1.main()
}
