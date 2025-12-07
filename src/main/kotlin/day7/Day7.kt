package day7

import com.aballano.mnemonik.memoize
import utils.getInput

fun main() {
    day7part1(getInput(7, true, 1))
    day7part1(getInput(7, false, 1))
    day7part2(getInput(7, true, 2))
    day7part2(getInput(7, false, 2))
}

private fun day7part1(lines: List<String>) {
    val startX = lines.first().indexOf('S')
    var splits = 0
    val beams = hashSetOf(startX)
    for (line in lines.asSequence().drop(1)) {
        val newBeams = mutableListOf<Int>()
        for (splitIndex in line.indices.asSequence().filter { line[it] == '^' }) {
            if (beams.remove(splitIndex)) {
                splits++
            }
            newBeams.add(splitIndex - 1)
            newBeams.add(splitIndex + 1)
        }
        beams.addAll(newBeams)
    }

    println(splits)
}

private fun day7part2(lines: List<String>) {
    println(Grid(lines).timelinesFrom(lines.first().indexOf('S'), 0))
}

private class Grid(private val lines: List<String>) {
    val timelinesFrom = ::timelinesFrom0.memoize()

    private fun timelinesFrom0(x: Int, yIn: Int): Long {
        var y = yIn
        while (y in lines.indices) {
            if (lines[y][x] == '^') {
                return timelinesFrom(x - 1, y) + timelinesFrom(x + 1, y)
            }
            y++
        }
        return 1
    }
}