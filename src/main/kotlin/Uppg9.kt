class Uppg9 {


	fun c() {
		val line = "10 13 16 21 30 45"
		val series = parseSeries(line)
		val matrix = mutableLists(series)

		val originalSeries = reconstructSeries(matrix)
		println(originalSeries)
	}

	fun a() {
		val lines = getLinesFromFile("Input9.txt")
		var sum = 0
		for (line in lines) {
			val series = parseSeries(line)
			val next = calculateSeries(series)
			println(next)
			sum += next
		}
		println("sum $sum")
	}
	fun b() {
		val lines = getLinesFromFile("Input9.txt")
		var sum = 0
		for (line in lines) {
			val series = parseSeries(line)
			val matrix = mutableLists(series)
			val next = reconstructSeries(matrix)[0]
			println(next)
			sum += next
		}
		println("sum $sum")
	}

	fun reconstructSeries(matrix: MutableList<MutableList<Int>>): List<Int> {
		for (i in matrix.size - 1 downTo 1) {
			val newElement = matrix[i - 1][0] - matrix[i][0]
			matrix[i - 1].add(0, newElement)
		}

		return matrix[0]
	}

	fun parseSeries(seriesStr: String): List<Int> {
		return seriesStr.split(" ").map { it.toInt() }
	}

	fun calculateSeries(series: List<Int>): Int {
		val matrix = mutableLists(series)

		// Backward Calculation
		for (i in matrix.size - 2 downTo 0) {
			matrix[i].add(0)
			val lastElement = matrix[i][matrix[i].size - 2] + matrix[i + 1][matrix[i + 1].size - 1]
			matrix[i][matrix[i].size - 1] = lastElement
		}

		// Result
		return matrix[0][matrix[0].size - 1]
	}

	private fun mutableLists(series: List<Int>): MutableList<MutableList<Int>> {
		val matrix = mutableListOf(series.toMutableList())

		// Populate the difference rows
		while (matrix.last().any { it != 0 }) {
			val lastRow = matrix.last()
			val nextRow = mutableListOf<Int>()
			for (i in 0 until lastRow.size - 1) {
				nextRow.add(lastRow[i + 1] - lastRow[i])
			}
			matrix.add(nextRow)
		}
		return matrix
	}

}