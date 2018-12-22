package net.wojtekk.aoc2018.days.day16

import net.wojtekk.aoc2018.utils.FileHelper.readLines
import java.lang.IllegalArgumentException

data class Registers(var a: Int, var b: Int, var c: Int, var d: Int) {
    fun get(n: Int): Int = when (n) {
        0 -> a
        1 -> b
        2 -> c
        3 -> d
        else -> throw Exception("Incorrect registry")
    }

    fun set(n: Int, value: Int): Unit = when (n) {
        0 -> a = value
        1 -> b = value
        2 -> c = value
        3 -> d = value
        else -> throw Exception("Incorrect registry")
    }
}

interface InstructionExecutor {
    val name: String
    fun execute(registers: Registers): Registers
}

abstract class AbstractInstruction(val opcode: Int, val regA: Int, val regB: Int, val regC: Int) : InstructionExecutor {
    protected abstract fun apply(a: Int, b: Int): Int

    fun result(registers: Registers, value: Int): Registers {
        val res = registers.copy()
        res.set(regC, value)
        return res
    }
}

abstract class AbstractInstructionRR(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstruction(opcode, regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(registers.get(regA), registers.get(regB))
        return result(registers, res)
    }
}

abstract class AbstractInstructionRI(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstruction(opcode, regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(registers.get(regA), regB)
        return result(registers, res)
    }
}

abstract class AbstractInstructionIR(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstruction(opcode, regA, regB, regC) {
    override fun execute(registers: Registers): Registers {
        val res = apply(regA, registers.get(regB))
        return result(registers, res)
    }
}


class Addr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "addr"
    override fun apply(a: Int, b: Int): Int = a + b
}

class Addi(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "addi"
    override fun apply(a: Int, b: Int): Int = a + b
}

class Mulr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "mulr"
    override fun apply(a: Int, b: Int): Int = a * b
}

class Muli(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "muli"
    override fun apply(a: Int, b: Int): Int = a * b
}

class Banr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "banr"
    override fun apply(a: Int, b: Int): Int = a and b
}

class Bani(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "bani"
    override fun apply(a: Int, b: Int): Int = a and b
}

class Borr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "borr"
    override fun apply(a: Int, b: Int): Int = a or b
}

class Bori(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "bori"
    override fun apply(a: Int, b: Int): Int = a or b
}

class Setr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "setr"
    override fun apply(a: Int, b: Int): Int = a
}

class Seti(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(opcode, regA, regB, regC) {
    override val name = "seti"
    override fun apply(a: Int, b: Int): Int = a
}

class Gtir(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(opcode, regA, regB, regC) {
    override val name = "gtir"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Gtri(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "gtri"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Gtrr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "gtrr"
    override fun apply(a: Int, b: Int): Int = if (a > b) 1 else 0
}

class Eqir(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionIR(opcode, regA, regB, regC) {
    override val name = "eqir"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

class Eqri(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRI(opcode, regA, regB, regC) {
    override val name = "eqri"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

class Eqrr(opcode: Int, regA: Int, regB: Int, regC: Int) : AbstractInstructionRR(opcode, regA, regB, regC) {
    override val name = "eqrr"
    override fun apply(a: Int, b: Int): Int = if (a == b) 1 else 0
}

object InstructionFactory {
    fun getAll() = listOf("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori",
            "setr", "seti", "gtrr", "gtir", "gtri", "eqrr", "eqir", "eqri")

    fun create(name: String, args: List<Int>): InstructionExecutor = when (name) {
        "addr" -> Addr(args[0], args[1], args[2], args[3])
        "addi" -> Addi(args[0], args[1], args[2], args[3])
        "mulr" -> Mulr(args[0], args[1], args[2], args[3])
        "muli" -> Muli(args[0], args[1], args[2], args[3])
        "banr" -> Banr(args[0], args[1], args[2], args[3])
        "bani" -> Bani(args[0], args[1], args[2], args[3])
        "borr" -> Borr(args[0], args[1], args[2], args[3])
        "bori" -> Bori(args[0], args[1], args[2], args[3])
        "setr" -> Setr(args[0], args[1], args[2], args[3])
        "seti" -> Seti(args[0], args[1], args[2], args[3])
        "gtrr" -> Gtrr(args[0], args[1], args[2], args[3])
        "gtir" -> Gtir(args[0], args[1], args[2], args[3])
        "gtri" -> Gtri(args[0], args[1], args[2], args[3])
        "eqrr" -> Eqrr(args[0], args[1], args[2], args[3])
        "eqir" -> Eqir(args[0], args[1], args[2], args[3])
        "eqri" -> Eqri(args[0], args[1], args[2], args[3])
        else -> throw IllegalArgumentException("Unknown instruction $name")
    }
}

object Day16p1 {
    fun main() {
        val path = "day16/input.txt"
        val res = readLines(path)
                .windowed(3, 3, false) { it.joinToString("") }
                .filter { it.contains("Before") }
                .map { it.split(Regex("[^0-9]+")).drop(1).dropLast(1).map(String::toInt).chunked(4) }
                .map(::validate)
                .filter { it >= 3 }
                .count()

        System.out.println("Answer: $res")
    }

    private fun validate(list: List<List<Int>>): Int {
        val before = Registers(list[0][0], list[0][1], list[0][2], list[0][3])
        val data = list[1]
        val after = Registers(list[2][0], list[2][1], list[2][2], list[2][3])

        return InstructionFactory.getAll()
                .map { InstructionFactory.create(it, data) }
                .filter { it.execute(before) == after }
                .count()
    }
}

fun main() {
    Day16p1.main()
}
