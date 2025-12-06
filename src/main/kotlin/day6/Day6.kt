package day6

import utils.getInput
import utils.ints

fun main() {
    day6part1(getInput(6, true, 1))
    day6part1(getInput(6, false, 1))
    day6part2(getInput(6, true, 2))
    day6part2(getInput(6, false, 2))
}

private fun day6part1(lines: List<String>) {
    println(parseInput1(lines).sumOf { it.answer() })
}

private val spacesThenDigits = " *[0-9]+".toRegex()

private fun day6part2(lines: List<String>) {
    var total = 0L
    for ((i, c) in lines.last().asSequence().withIndex().filter { (_, c) -> c != ' ' }) {
        val nums = lines.dropLast(1).map { spacesThenDigits.matchAt(it, i)!!.value }
        val verticalNums = (0..nums.maxOf { it.lastIndex }).map { digitI ->
            nums.asSequence().mapNotNull { it.getOrNull(digitI)?.takeIf(Char::isDigit) }.joinToString("").toInt()
        }
        total += Problem(verticalNums, c).answer()
    }
    println(total)
}

private val spaces = " +".toRegex()

private fun parseInput1(lines: List<String>): List<Problem> {
    val rows = lines.asSequence().take(lines.size - 1).map { it.ints() }.toList()
    return lines.last().splitToSequence(spaces).filter(String::isNotBlank).mapIndexed { i, operator ->
        Problem(rows.map { it[i] }, operator.single())
    }.toList()
}

private data class Problem(val inputs: List<Int>, val operator: Char) {
    fun answer(): Long = when (operator) {
        '+' -> inputs.sumOf { it.toLong() }
        '*' -> inputs.fold(1L) { a, b -> a * b.toLong() }
        else -> error("Unknown operator $operator")
    }
}