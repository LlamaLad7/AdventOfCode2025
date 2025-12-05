package day5

import utils.getInput
import utils.split
import java.util.SortedMap

fun main() {
    day5part1(getInput(5, true, 1))
    day5part1(getInput(5, false, 1))
    day5part2(getInput(5, true, 2))
    day5part2(getInput(5, false, 2))
}

private fun day5part1(lines: List<String>) {
    val (ranges, nums) = parseInput(lines)
    println(nums.count { num -> ranges.any { range -> num in range } })
}

private fun day5part2(lines: List<String>) {
    val (ranges, _) = parseInput(lines)
    val changes = ranges.toChanges()

    var result = 0L
    var numFresh = 0
    var pos = changes.firstKey()

    for ((num, changing) in changes) {
        if (numFresh > 0) {
            result += num - pos
        }
        numFresh += changing.starting
        numFresh -= changing.ending
        if (numFresh == 0) {
            result++
        }
        pos = num
    }
    println(result)
}

private fun parseInput(lines: List<String>) = lines.split("").let { (ranges, nums) ->
    ranges.map { range ->
        range.split('-').let { (a, b) -> a.toLong()..b.toLong() }
    } to nums.map(String::toLong)
}

private fun List<LongRange>.toChanges(): SortedMap<Long, Changes> {
    val result = sortedMapOf<Long, Changes>()
    for (range in this) {
        result.getOrPut(range.first) { Changes(0, 0) }.starting++
        result.getOrPut(range.last) { Changes(0, 0) }.ending++
    }
    return result
}

private data class Changes(var starting: Int, var ending: Int)