package day4

import utils.getInput

fun main() {
    day4part1(getInput(4, true, 1))
    day4part1(getInput(4, false, 1))
    day4part2(getInput(4, true, 2))
    day4part2(getInput(4, false, 2))
}

private fun day4part1(lines: List<String>) {
    println(Grid(lines).accessible().count())
}

private fun day4part2(lines: List<String>) {
    var removed = 0
    val grid = Grid(lines)
    while (true) {
        val accessible = grid.accessible().iterator()
        if (!accessible.hasNext()) {
            break
        }
        for (pos in accessible) {
            grid.remove(pos)
            removed++
        }
    }
    println(removed)
}

private class Grid(lines: List<String>) {
    private val grid = lines.map { it.toCharArray() }

    fun accessible(): Sequence<Pos> = sequence {
        for ((y, row) in grid.withIndex()) {
            for ((x, c) in row.withIndex()) {
                if (c != '@') {
                    continue
                }
                val pos = Pos(x, y)
                if (pos.adjacent().count { (adjX, adjY) -> grid.getOrNull(adjY)?.getOrNull(adjX) == '@' } < 4) {
                    yield(pos)
                }
            }
        }
    }

    fun remove(pos: Pos) {
        grid[pos.y][pos.x] = '.'
    }
}

private data class Pos(val x: Int, val y: Int) {
    fun adjacent() = sequenceOf(
        Pos(x - 1, y - 1),
        Pos(x, y - 1),
        Pos(x + 1, y - 1),
        Pos(x - 1, y),
        Pos(x + 1, y),
        Pos(x - 1, y + 1),
        Pos(x, y + 1),
        Pos(x + 1, y + 1),
    )
}