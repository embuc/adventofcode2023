

fun main(args: Array<String>) {
	val uppg = Uppg6();
	uppg.b();
}

fun getLinesFromFile(fileName: String): List<String> {
	val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
		?: throw IllegalArgumentException("File not found")
	return inputStream.bufferedReader().use { it.readLines() }
}