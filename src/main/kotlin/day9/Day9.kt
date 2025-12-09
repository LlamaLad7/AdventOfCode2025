package day9

import utils.allPairs
import utils.getInput
import utils.ints
import java.util.*
import kotlin.math.absoluteValue

fun main() {
    day9part1(getInput(9, true, 1))
    day9part1(getInput(9, false, 1))
    day9part2(getInput(9, true, 2))
    day9part2(getInput(9, false, 2))
}

private fun day9part1(lines: List<String>) {
    val points = lines.map { it.ints().let { (a, b) -> Point(a, b) } }
    println(
        points.allPairs()
            .maxOf { (a, b) -> areaOf(a, b) }
    )
}

private fun day9part2(lines: List<String>) {
    val points = lines.map { it.ints().let { (a, b) -> Point(a, b) } }
    val verticalEdges = TreeMap<Double, MutableList<IntRange>>()
    val horizontalEdges = TreeMap<Double, MutableList<IntRange>>()
    for ((a, b) in points.zipWithNext().asSequence() + (points.last() to points.first())) {
        if (a.x == b.x) {
            // Vertical edge
            verticalEdges.getOrPut(a.x.toDouble(), ::mutableListOf).add(rangeOf(a.y, b.y))
        } else {
            // Horizontal edge
            horizontalEdges.getOrPut(a.y.toDouble(), ::mutableListOf).add(rangeOf(a.x, b.x))
        }
    }
    var bestArea = Long.MIN_VALUE
    outer@ for ((corner1, corner2) in points.allPairs()) {
        val area = areaOf(corner1, corner2)
        if (area < bestArea) {
            continue
        }
        val xRange = rangeOf(corner1.x, corner2.x)
        val yRange = rangeOf(corner1.y, corner2.y)
        for (edge in verticalEdges.subMap(
            xRange.first.toDouble(),
            false,
            xRange.last.toDouble(),
            false
        ).values.asSequence().flatten()) {
            if (edge overlapsWith yRange.exclusiveEnds) {
                //  This ruins our rectangle
                continue@outer
            }
        }
        for (edge in horizontalEdges.subMap(
            yRange.first.toDouble(),
            false,
            yRange.last.toDouble(),
            false
        ).values.asSequence().flatten()) {
            if (edge overlapsWith xRange.exclusiveEnds) {
                //  This ruins our rectangle
                continue@outer
            }
        }

        // Now we need to make sure it's internal
        val middleX = (corner1.x + corner2.x) / 2.0
        val middleY = (corner1.y + corner2.y) / 2.0
        val isInternal =
            verticalEdges.tailMap(middleX).values.asSequence()
                .flatten()
                .filter { it.first <= middleY && middleY <= it.last }
                .count() % 2 == 1
        if (isInternal && area > bestArea) {
            bestArea = area
        }
    }
    println(bestArea)
}

private data class Point(val x: Int, val y: Int)

private fun rangeOf(a: Int, b: Int) = if (a <= b) a..b else b..a

private fun areaOf(a: Point, b: Point) = ((a.x - b.x).absoluteValue + 1).toLong() * ((a.y - b.y).absoluteValue + 1)

private val IntRange.exclusiveEnds get() = first + 1..<last

infix fun IntRange.overlapsWith(other: IntRange) = last >= other.first && first <= other.last