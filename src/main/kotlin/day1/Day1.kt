package day1

import utils.getInput
import utils.ints

fun main() {
    day1part1(getInput(1, true, 1))
    day1part1(getInput(1, false, 1))
    day1part2(getInput(1, true, 2))
    day1part2(getInput(1, false, 2))
}

private fun day1part1(lines: List<String>) {
    var pos = 50
    var times0 = 0
    for (line in lines) {
        var rot = line.ints().single()
        if (line.startsWith('L')) {
            rot *= -1
        }
        pos = (pos + rot).mod(100)
        if (pos == 0) {
            times0++
        }
    }
    println(times0)
}

private fun day1part2(lines: List<String>) {
    var pos = 50
    var times0 = 0
    for (line in lines) {
        var rot = line.ints().single()
        times0 += rot / 100
        rot %= 100
        if (line.startsWith('L')) {
            if (rot >= pos && pos != 0) {
                times0++
            }
            pos -= rot
        } else {
            pos += rot
            if (pos >= 100) {
                times0++
            }
        }
        pos = pos.mod(100)
    }
    println(times0)
}