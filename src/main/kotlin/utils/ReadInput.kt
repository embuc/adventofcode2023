package utils

import java.io.File

fun readInputAsString(task: String): String {
	val inputStream = File("src/main/resources/$task").inputStream()
	return inputStream.bufferedReader().use { it.readText() }.trimIndent()
}

fun readInputAsListOfStrings(fileName: String): List<String> {
	val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
		?: throw IllegalArgumentException("File not found")
	return inputStream.bufferedReader().use { it.readLines() }
}