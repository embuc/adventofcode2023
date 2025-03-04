package y2022

import Task

//--- Day 14: Regolith Reservoir ---
class Task14(val input: List<String>) : Task {

	override fun a(): Any {
		val grid = parseInput(input)
		val gridMap = buildGrid(grid, floor = false)
		val sandCount = simulateSand(gridMap, gridMap.keys.maxOf { it.second }, floor = false)
		return sandCount
	}

	override fun b(): Any {
		val grid = parseInput(input)
		val gridMap = buildGrid(grid, floor = true)
		val sandCount = simulateSand(gridMap, gridMap.keys.maxOf { it.second }, floor = true)
		return sandCount
	}

	fun parseInput(inputLines: List<String>): List<List<Pair<Int, Int>>> {
		val paths = mutableListOf<List<Pair<Int, Int>>>()
		for (line in inputLines) {
			val points = line.split(" -> ")
			val path = points.map { point ->
				val (x, y) = point.split(",").map { it.toInt() }
				x to y
			}
			paths.add(path)
		}
		return paths
	}

	fun buildGrid(paths: List<List<Pair<Int, Int>>>, floor: Boolean): MutableMap<Pair<Int, Int>, Char> {
		val grid = mutableMapOf<Pair<Int, Int>, Char>()
		for (path in paths) {
			for (i in 0 until path.size - 1) {
				val (x1, y1) = path[i]
				val (x2, y2) = path[i + 1]
				if (x1 == x2) {
					for (y in minOf(y1, y2)..maxOf(y1, y2)) {
						grid[x1 to y] = '#'
					}
				} else {
					for (x in minOf(x1, x2)..maxOf(x1, x2)) {
						grid[x to y1] = '#'
					}
				}
			}
		}
		if (floor) {
			val maxY = grid.keys.maxOf { it.second } + 2
			//print debug grid with floor to see why these values are chosen
			val minX = grid.keys.minOf { it.first } - 148
			val maxX = grid.keys.maxOf { it.first } + 136
			for (x in minX..maxX) {
				grid[x to maxY] = '#'
			}
		}
		return grid
	}

	fun simulateSand(grid: MutableMap<Pair<Int, Int>, Char>, maxY: Int, floor: Boolean): Int {
		var sandCount = 0
		var maxY = if (floor) maxY + 2 else maxY
		while (true) {
			var x = 500
			var y = 0
			while (y <= maxY || grid[x to y + 1] == 'o') {
				if ((x to y + 1) !in grid) {
					y += 1
				} else if ((x - 1 to y + 1) !in grid) {
					x -= 1
					y += 1
				} else if ((x + 1 to y + 1) !in grid) {
					x += 1
					y += 1
				} else {
					grid[x to y] = 'o'
					sandCount += 1
					break
				}
			}
			if (y > maxY || grid[500 to 0] == 'o') {
				break
			}
		}
		return sandCount
	}

	fun visualizeGridCropped(grid: MutableMap<Pair<Int, Int>, Char>) {

		if (grid.isEmpty()) {
			println("Grid is empty.")
			return
		}

		// Determine the bounds of the grid, adding a buffer
		val minX = grid.keys.minOf { it.first }
		val maxX = grid.keys.maxOf { it.first }
		val minY = grid.keys.minOf { it.second }
		val maxY = grid.keys.maxOf { it.second }

		val bufferedMinX = minX - 1
		val bufferedMaxX = maxX + 1
		val bufferedMinY = minY - 1
		val bufferedMaxY = maxY + 1
		// Print the grid
		for (y in bufferedMinY..bufferedMaxY) {
			for (x in bufferedMinX..bufferedMaxX) {
				val cell = grid[x to y] ?: '.' // Use '.' for 'air'
				print(cell)
			}
			println()
		}
	}
}