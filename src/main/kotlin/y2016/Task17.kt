package y2016

import Task
import utils.manhattanDistance
import java.security.MessageDigest
import java.util.*

//--- Day 17: Two Steps Forward ---
class Task17(val input: String) : Task {

	val grid = Array(4) { CharArray(4) { ' ' } }
	private val md: MessageDigest = MessageDigest.getInstance("MD5")
	data class State(val position: Pair<Int, Int>, val hash: String)

	override fun a(): Any {
		return aStarConditional(grid, Pair(0, 0), Pair(3, 3)).last().hash.substring(input.length)
	}

	private fun getNeighbors(grid: Array<CharArray>, position: Pair<Int, Int>): List<Pair<Int, Int>> {
		val (row, col) = position
		val neighbors = mutableListOf<Pair<Int, Int>>()
		//UP, DOWN, LEFT, RIGHT
		val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

		for ((dr, dc) in directions) {
			val nr = row + dr
			val nc = col + dc
			if (nr >= 0 && nr < grid.size && nc >= 0 && nc < grid[0].size) {
				neighbors.add(Pair(nr, nc))
			}
		}

		return neighbors
	}

	private fun doorRules(hash: String): Map<String, Boolean> {
		val newHash = md.digest(hash.toByteArray()).joinToString("") { "%02x".format(it) }
		return mapOf(
			"UP" to (newHash[0] in 'b'..'f'),
			"DOWN" to (newHash[1] in 'b'..'f'),
			"LEFT" to (newHash[2] in 'b'..'f'),
			"RIGHT" to (newHash[3] in 'b'..'f')
		)
	}

	private fun canMove(currentPos: Pair<Int, Int>, neighbor: Pair<Int, Int>, doorStates: Map<String, Boolean>): Boolean {
		if (neighbor.first < 0 || neighbor.first >= grid.size || neighbor.second < 0 || neighbor.second >= grid[0].size) {
			return false
		}
		if(doorStates["UP"] == true && neighbor.first < currentPos.first) return true
		if(doorStates["DOWN"] == true && neighbor.first > currentPos.first) return true
		if(doorStates["LEFT"] == true && neighbor.second < currentPos.second) return true
		if(doorStates["RIGHT"] == true && neighbor.second > currentPos.second) return true
		return false
	}

	private fun reconstructPath(cameFrom: MutableMap<State, State>, current: State): List<State> {
		val path = mutableListOf<State>()
		var currentPath = current

		while (cameFrom.containsKey(currentPath)) {
			path.add(currentPath)
			currentPath = cameFrom[currentPath]!!
		}
		path.add(currentPath)
		return path.reversed()
	}

	// A* algorithm
	private fun aStarConditional(grid: Array<CharArray>, start: Pair<Int, Int>, target: Pair<Int, Int>): List<State> {

		val startState = State(start, input)
		val openSet = PriorityQueue<Pair<Int, State>>(compareBy { it.first })
		openSet.add(Pair(manhattanDistance(startState.position, target), startState))

		val cameFrom = mutableMapOf<State, State>()
		val costSoFar = mutableMapOf(startState to 0)

		while (openSet.isNotEmpty()) {
			val (_, current) = openSet.poll()
			if (current.position == target) {
				return reconstructPath(cameFrom, current)
			}

			val newDoorStates = doorRules(current.hash)
			//TODO refactor this to use getPossibleMoves
			for (neighbor in getNeighbors(grid, current.position)) {
				val suffix = getMoveSuffix(current.position, neighbor)
				val newState = State(neighbor, current.hash + suffix)
				if (canMove(current.position, neighbor, newDoorStates)) {
					val newCost = costSoFar[current]!! + 1
					if (!costSoFar.containsKey(newState) || newCost < costSoFar[newState]!!) {
						costSoFar[newState] = newCost
						val f = newCost + manhattanDistance(newState.position, target)
						openSet.add(Pair(f, newState))
						cameFrom[newState] = current
					}
				}
			}
		}
		return listOf() // No path :/
	}

	private fun getMoveSuffix(position: Pair<Int, Int>, neighbor: Pair<Int, Int>): String {
		return when {
			neighbor.first < position.first -> "U"
			neighbor.first > position.first -> "D"
			neighbor.second < position.second -> "L"
			else -> "R"
		}
	}

	override fun b(): Any {
		return findLongestPath(Pair(0, 0), Pair(3, 3))
	}

	private fun findLongestPath(start: Pair<Int, Int>, target: Pair<Int, Int>): Int {
		var maxLength = -1

		fun dfs(current: State) {
			// If we reached the target, update max length if this path is longer
			if (current.position == target) {
				val pathLength = current.hash.length - input.length
				maxLength = maxOf(maxLength, pathLength)
				return
			}

			// Try all possible moves from current position
			for (nextState in getAvailableMoves(current)) {
				dfs(nextState)
			}
		}

		dfs(State(start, input))
		return maxLength
	}

	private fun getAvailableMoves(state: State): List<State> {
		val (row, col) = state.position
		// Get the current door states based on the full path hash
		val doors = doorRules(state.hash)

		return listOf(
			Triple(-1, 0, "UP"),
			Triple(1, 0, "DOWN"),
			Triple(0, -1, "LEFT"),
			Triple(0, 1, "RIGHT")
		).mapNotNull { (dr, dc, dir) ->
			val newRow = row + dr
			val newCol = col + dc
			// Only return moves where:
			// 1. The new position is within bounds
			// 2. The door in that direction is open according to the hash
			if (newRow in 0..3 && newCol in 0..3 && doors[dir] == true) {
				State(Pair(newRow, newCol), state.hash + dir[0])
			} else null
		}
	}

}
