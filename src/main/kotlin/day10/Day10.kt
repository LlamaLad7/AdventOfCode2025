package day10

import com.microsoft.z3.Expr
import com.microsoft.z3.IntSort
import utils.getInput
import utils.ints
import utils.z3.z3Optimize

fun main() {
    day10part1(getInput(10, true, 1))
    day10part1(getInput(10, false, 1))
    day10part2(getInput(10, true, 2))
    day10part2(getInput(10, false, 2))
}

private fun day10part1(lines: List<String>) {
    val problems = parseInput1(lines)
    println(problems.sumOf { it.solve() })
}

private fun day10part2(lines: List<String>) {
    val problems = parseInput2(lines)
    println(problems.sumOf { it.solve() })
}

private fun parseInput1(lines: List<String>): List<Problem1> = lines.map { line ->
    val parts = line.split(' ')
    val desired = parts.first().removeSurrounding("[", "]").replace('.', '0').replace('#', '1').reversed().toInt(2)
    val buttons = parts.subList(1, parts.size - 1).map { it.ints().setBits() }
    Problem1(desired, buttons)
}

private fun parseInput2(lines: List<String>): List<Problem2> = lines.map { line ->
    val parts = line.split(' ')
    val desired = parts.last().ints()
    val buttons = parts.subList(1, parts.size - 1).map { it.ints() }
    Problem2(desired, buttons)
}

private data class Problem1(val desired: Int, val buttons: List<Int>) {
    fun solve(): Int {
        var best = Int.MAX_VALUE
        for (i in 0..<(1 shl buttons.size)) {
            if (i.countOneBits() > best) {
                continue
            }
            var working = 0
            for ((buttonIndex, buttonMask) in buttons.withIndex()) {
                if (i and (1 shl buttonIndex) == 0) {
                    continue
                }
                working = working xor buttonMask
            }
            if (working == desired && i.countOneBits() < best) {
                best = i.countOneBits()
            }
        }
        return best
    }
}

private data class Problem2(val desired: List<Int>, val buttons: List<List<Int>>) {
    fun solve(): Long {
        val buttonIndices = hashMapOf<Int, MutableList<Int>>()
        for ((i, button) in buttons.withIndex()) {
            for (affectedIndex in button) {
                buttonIndices.getOrPut(affectedIndex) { mutableListOf() }.add(i)
            }
        }
        return z3Optimize {
            val presses: List<Expr<IntSort>> = buttons.indices.map { "button$it".z3Int }
            for (press in presses) {
                constraints(press ge 0.z3Int)
            }
            val total = presses.reduce { a, b -> a + b }
            minimize(total)

            for ((index, buttons) in buttonIndices) {
                constraints(buttons.asSequence().map { presses[it] }
                    .reduce { a, b -> a + b } eq desired[index].z3Int)
            }

            check(solve())
            total.solution
        }
    }
}

private fun List<Int>.setBits(): Int = fold(0) { acc, bit -> acc or (1 shl bit) }