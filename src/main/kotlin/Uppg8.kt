import kotlin.math.abs

const val steps = "LRLRRRLRRLRRRLRRRLLLLLRRRLRLRRLRLRLRRLRRLRRRLRLRLRRLLRLRRLRRLRRLRRRLLRRRLRRRLRRLRLLLRRLRRRLRLRRLRRRLRRLRLLLRRRLRRLRRLRRRLRRRLRRRLRLRLRLRRRLRRRLLLRRLLRRRLRLRLRRRLRRRLRRLRRRLRLRLLRRRLRLRRLRLRLRRLLLRRRLRRRLRRLRRLRLRRLLRRLRRRLRRRLLRRRLRRLRLLRRLRLRRLLRRRLLLLRRLRRRLRLRRLLRLLRRRLLRRLLRRRLRRRLRRLLRLRLLRRLLRLLLRRRR"

class Uppg8 {

	fun a() {
		val lines = getLinesFromFile("Input8.txt")
		val dict = parseToMap(lines)
		val path = traverseMap(dict, "AAA", steps)
		println(path)
		println(path.size)
	}

	fun b() {
		val lines = getLinesFromFile("Input8.txt")
		val dict = parseToMap(lines)
		val commonStepCount = findCommonStepCount(dict, steps)
		println("Common step count: $commonStepCount")
	}

	data class ValuePair(val left: String, val right: String)


	fun gcd(a: Long, b: Long): Long {
		return if (b == 0L) a else gcd(b, a % b)
	}

	fun lcm(a: Long, b: Long): Long {
		return abs(a * b) / gcd(a, b)
	}

	fun findPathLengthToZ(map: Map<String, ValuePair>, startKey: String, steps: String): Long {
		var currentKey = startKey
		var count = 0L
		var stepsIterator = steps.iterator()

		while (!currentKey.endsWith("Z")) {
			if (!stepsIterator.hasNext()) {
				stepsIterator = steps.iterator() // Restart the steps
			}
			val step = stepsIterator.next()
			val pair = map[currentKey]!!
			currentKey = if (step == 'L') pair.left else pair.right
			count++
		}

		return count
	}

	fun findCommonStepCount(map: Map<String, ValuePair>, steps: String): Long {
		val startingNodes = map.keys.filter { it.endsWith("A") }
		println("startingNodes $startingNodes")
		val pathLengths = startingNodes.map { findPathLengthToZ(map, it, steps) }
		println(pathLengths)
		println(pathLengths.size)
		// Calculate LCM of all path lengths
		return findLCM(pathLengths)
	}

	private fun findLCM(pathLengths: List<Long>): Long {
		return pathLengths.reduce { acc, length -> lcm(acc, length) }
	}

	fun parseToMap(strings: List<String>): Map<String, ValuePair> {
		return strings.mapNotNull { str ->
			val parts = str.split("=").map { it.trim() }
			if (parts.size == 2) {
				val key = parts[0]
				val valuePart = parts[1].removeSurrounding("(", ")")
				val values = valuePart.split(",").map { it.trim() }
				if (values.size == 2) key to ValuePair(values[0], values[1]) else null
			} else {
				null
			}
		}.toMap()
	}

	fun traverseMap(map: Map<String, ValuePair>, startKey: String, steps: String): List<String> {
		var currentKey = startKey
		val path = mutableListOf<String>()
		println("START: currentKey: $currentKey")

		// Custom generator for cycling through steps
		fun cycleSteps() = sequence {
			while (true) yieldAll(steps.asIterable())
		}

		val stepsIterator = cycleSteps().iterator()
		var i = 0
		while (stepsIterator.hasNext()) {
			i++
			path.add(currentKey)
			val step = stepsIterator.next()
			val pair = map[currentKey] ?: break // Break if the current key is not found
//			println("step: $step, pair: $pair")

			currentKey = if (step == 'L') pair.left else pair.right
//			println("currentKey: $currentKey")
			if (currentKey == "ZZZ") break
		}

		return path
	}

}