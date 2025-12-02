package day2

import utils.getInput

fun main() {
    day2part1(getInput(2, true, 1))
    day2part1(getInput(2, false, 1))
    day2part2(getInput(2, true, 2))
    day2part2(getInput(2, false, 2))
}

private fun day2part1(lines: List<String>) {
    val ranges =
        lines.single().splitToSequence(',').map { it.split('-').map(String::toLong).let { (a, b) -> a..b } }.toList()
    println(
        ranges.sumOf { range ->
            range.asSequence()
                .filter { num ->
                    val text = num.toString()
                    text.length % 2 == 0 && text.substring(0..<text.length / 2) == text.substring(text.length / 2)
                }
                .sum()
        }
    )
}

private fun day2part2(lines: List<String>) {
    val ranges =
        lines.single().splitToSequence(',').map { it.split('-').map(String::toLong).let { (a, b) -> a..b } }.toList()
    println(
        ranges.sumOf { range ->
            range.asSequence()
                .filter(::isValid)
                .sum()
        }
    )
}

private fun isValid(num: Long): Boolean {
    val text = num.toString()
    for (chunkSIze in 1..text.length / 2) {
        if (text.length % chunkSIze != 0) {
            continue
        }
        if (text.chunkedSequence(chunkSIze).distinct().count() == 1) {
            return true
        }
    }
    return false
}