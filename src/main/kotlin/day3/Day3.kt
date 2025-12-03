package day3

import com.aballano.mnemonik.memoize
import utils.getInput

fun main() {
    day3part1(getInput(3, true, 1))
    day3part1(getInput(3, false, 1))
    day3part2(getInput(3, true, 2))
    day3part2(getInput(3, false, 2))
}

private fun day3part1(lines: List<String>) {
    println(lines.sumOf { it.bestScore() })
}

private fun day3part2(lines: List<String>) {
    println(lines.sumOf { bestScoreWith(it, 12) })
}

private fun String.bestScore(): Int {
    var best = Int.MIN_VALUE
    for (a in 0..<lastIndex) {
        for (b in (a + 1)..lastIndex) {
            best = maxOf(best, this[a].digitToInt() * 10 + this[b].digitToInt())
        }
    }
    return best
}

private fun bestScoreWith(word: String, numDigits: Int): Long {
    lateinit var bestWith: (Int, Int) -> Long

    fun bestWith0(startIndex: Int, numLeft: Int): Long {
        if (numLeft == 0 || startIndex == word.length) {
            return 0L
        }
        return (startIndex..word.length - numLeft).maxOf { choice ->
            word[choice].digitToInt().timesTenToThe(numLeft - 1) + bestWith(choice + 1, numLeft - 1)
        }
    }

    bestWith = ::bestWith0.memoize()
    return bestWith(0, numDigits)
}

private fun Int.timesTenToThe(exp: Int): Long = (1..exp).fold(this.toLong()) { acc, _ -> acc * 10 }