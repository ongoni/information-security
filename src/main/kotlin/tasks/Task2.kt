package tasks

import toByteString
import java.io.File

object Task2 {

    private const val defaultSearchFolder = "tests/task2"
    private val testFileTypes = mapOf(
            "bmp" to "66 77",                     // 42 4D
            "png" to "-119 80 78 71 13 10 26 10", // 89 50 4E 47 0D 0A 1A 0A
            "rar" to "82 97 114 33 26 7 0",       // 52 61 72 21 1A 07 00
            "docx" to "80 75 3 4"                  // 50 4B 03 04
    )

    fun run(type: String = "rar") {
        println("Task 2:")
        Task2.findFiles(type).forEach {
            println(it.path)
        }
    }

    private fun findFiles(type: String): List<File> {
        val prefix = testFileTypes[type] ?: return listOf()

        val found = mutableListOf<File>()
        search(File(defaultSearchFolder)) {
            x -> found.add(x)
        }

        return found
                .filter { x -> x.readBytes()
                        .toByteString()
                        .startsWith(prefix)
                }
    }

    private fun search(file: File, handler: (File) -> (Unit)) {
        file.listFiles().forEach {
            if (it.isDirectory) {
                search(it, handler)
            } else {
                handler(it)
            }
        }
    }

}