package day11

import com.aballano.mnemonik.memoize
import utils.getInput

fun main() {
    day11part1(getInput(11, true, 1))
    day11part1(getInput(11, false, 1))
    day11part2(getInput(11, true, 2))
    day11part2(getInput(11, false, 2))
}

private fun day11part1(lines: List<String>) {
    val start = parseInput(lines, "you")

    fun waysFrom(node: Node): Long = when (node) {
        is Node.Output -> 1
        is Node.Device -> node.connections.sumOf(::waysFrom)
    }

    println(waysFrom(start))
}

private fun day11part2(lines: List<String>) {
    val start = parseInput(lines, "svr")

    lateinit var waysFrom: (Node, Boolean, Boolean) -> Long
    fun waysFrom0(node: Node, visitedDac: Boolean, visitedFft: Boolean): Long {
        val newVisitedDac = visitedDac || node.label == "dac"
        val newVisitedFft = visitedFft || node.label == "fft"
        return when (node) {
            is Node.Output -> if (visitedDac && visitedFft) 1 else 0
            is Node.Device -> node.connections.sumOf { waysFrom(it, newVisitedDac, newVisitedFft) }
        }
    }
    waysFrom = ::waysFrom0.memoize()

    println(waysFrom(start, false, false))
}

private fun parseInput(lines: List<String>, start: String): Node.Device {
    val nodeConnections = lines.associate { line ->
        val (label, connections) = line.split(": ")
        label to connections.split(' ')
    }
    val nodes = hashMapOf<String, Node.Device>()
    for ((label, connections) in nodeConnections) {
        val node = nodes.computeIfAbsent(label, Node::Device)
        for (connection in connections) {
            if (connection == "out") {
                node.connections.add(Node.Output)
            } else {
                node.connections.add(nodes.computeIfAbsent(connection, Node::Device))
            }
        }
    }
    return nodes.getValue(start)
}

private sealed class Node(val label: String) {
    class Device(label: String) : Node(label) {
        val connections = mutableListOf<Node>()
    }

    object Output : Node("out")
}