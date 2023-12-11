import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GalaxyTests {

	@Test
	fun testExpandGrid() {
		val uppg11 = Uppg11()
		val input = listOf(
			"...#......",
			".......#..",
			"#.........",
			"..........",
			"......#...",
			".#........",
			".........#",
			"..........",
			".......#..",
			"#...#....."
		)
		val expandedProcessedInput = listOf(
			"....1........",
			".........2...",
			"3............",
			".............",
			".............",
			"........4....",
			".5...........",
			"............6",
			".............",
			".............",
			".........7...",
			"8....9......."
		)

		val expandedGrid = uppg11.expandGrid(input)
		uppg11.printGrid(expandedGrid)
		uppg11.printGridCoord(expandedGrid)

		// Define your expected result
		val expected = expandedProcessedInput

		// Convert expandedGrid to a comparable format if necessary
		val actual = expandedGrid.map { row ->
			row.joinToString("") { it.label }
		}

		assertEquals(expected, actual)
	}

	@Test
	fun findAllGalaxyPairs() {
		val uppg11 = Uppg11()
		val input = listOf(
			"...#......",
			".......#..",
			"#.........",
			"..........",
			"......#...",
			".#........",
			".........#",
			"..........",
			".......#..",
			"#...#....."
		)
		val expandedGrid = uppg11.expandGrid(input)
		val galaxyPairs = uppg11.findAllGalaxyPairs(expandedGrid)
		println(galaxyPairs)
		assertEquals(36, galaxyPairs.size)
	}

	@Test
	fun findShortestPaths() {
		val uppg11 = Uppg11()
		val input = listOf(
			"...#......",
			".......#..",
			"#.........",
			"..........",
			"......#...",
			".#........",
			".........#",
			"..........",
			".......#..",
			"#...#....."
		)
		val expandedGrid = uppg11.expandGrid(input)
		val galaxyPairs = uppg11.findAllGalaxyPairs(expandedGrid)
		println(galaxyPairs)
		uppg11.printGrid(expandedGrid)
		var sum = 0;
		for (pair in galaxyPairs) {
			val path = uppg11.bfs(pair.first, pair.second, expandedGrid)
			println("Path from ${pair.first.label} to ${pair.second.label} length: ${path.size}")
			println(path)
			sum +=path.size-1
		}
		println("sum: $sum")
		assertEquals(36, galaxyPairs.size)
		assertEquals(374, sum)
	}
}