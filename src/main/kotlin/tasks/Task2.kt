package tasks

import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import search
import java.io.File

object Task2 {

    private const val defaultSearchFolder = "tests/task2"

    fun run(type: String? = null) {
        println("Task 2:")
        Task2.findFiles(type).forEach {
            println("${it.first.path} - ${it.second}")
        }
    }

    private fun findFiles(type: String?): List<Pair<File, String>> {
        val found = mutableListOf<Pair<File, String>>()
        search(File(defaultSearchFolder)) {
            x -> found.add(x to getType(x))
        }

        return if (!type.isNullOrEmpty()) {
            found.filter { x -> x.second == type }
        } else {
            found
        }
    }

    private fun getType(file: File): String {
        val metadata = Metadata()
        metadata.add(Metadata.MESSAGE_PREFIX, file.name)

        return AutoDetectParser()
                .detector
                .detect(TikaInputStream.get(file), metadata)
                .toString()

//        return Files.probeContentType(Paths.get(file.path))
    }

}