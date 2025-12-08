package day8

import utils.getInput
import utils.ints
import java.util.*

fun main() {
    day8part1(getInput(8, true, 1), true)
    day8part1(getInput(8, false, 1), false)
    day8part2(getInput(8, true, 2))
    day8part2(getInput(8, false, 2))
}

private fun day8part1(lines: List<String>, test: Boolean) {
    println(connect(lines, if (test) 10 else 1000).take(3).let { (a, b, c) -> a * b * c })
}

private fun day8part2(lines: List<String>) {
    lateinit var lastConnected: Pair<Point, Point>
    connect(lines, Int.MAX_VALUE) { a, b ->
        lastConnected = a to b
    }
    println(lastConnected.let { (a, b) -> a.x * b.x })
}

private inline fun connect(
    lines: List<String>,
    maxConnections: Int,
    onConnect: (Point, Point) -> Unit = { _, _ -> }
): List<Int> {
    val points = lines.map { it.ints().let { (x, y, z) -> Point(x, y, z) } }
    val pairs = PriorityQueue<Pair<Point, Point>>(compareBy({ (a, b) -> a.distanceTo(b) }))
    for ((i, a) in points.withIndex()) {
        for (b in points.subList(i + 1, points.size)) {
            pairs.add(a to b)
        }
    }
    for (i in 1..maxConnections) {
        val (aSource, bSource) = pairs.poll() ?: break
        val a = aSource.root
        val b = bSource.root
        if (a == b) {
            // Same circuit already
            continue
        }
        onConnect(aSource, bSource)
        if (a.size >= b.size) {
            b.parent = a
            a.size += b.size
        } else {
            a.parent = b
            b.size += a.size
        }
    }
    return points.asSequence()
        .map { it.root }
        .distinct()
        .map { it.size }
        .sortedDescending()
        .toList()
}

private class Point(val x: Int, val y: Int, val z: Int) {
    var parent: Point? = null
    var size: Int = 1

    val root: Point get() = parent?.root ?: this

    fun distanceTo(other: Point): Long = (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()
}

private fun Int.squared() = this.toLong() * this