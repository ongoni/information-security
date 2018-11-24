package tasks

import md5.MD5Hash
import java.io.File

object Task1 {

    private const val defaultFolderPath = "tests/task1"
    private const val defaultFileName = "default.txt"
    private const val defaultFileFullPath = "$defaultFolderPath/$defaultFileName"

    fun run(text: String = "some text to get control sum of") {
        println("Task 1:")
        println(Task1.getHash(text))
    }

    private fun getHash(text: String): String {
        preprocess(text)

        return MD5Hash().getHash(
                File(defaultFileFullPath).readText()
        )
    }

    private fun preprocess(text: String) {
        if (!File(defaultFileFullPath).exists()) {
            val folder = File(defaultFolderPath)
            folder.mkdirs()
            val file = File(folder, defaultFileName)
            file.createNewFile()

            file.writeText(text)
        }
    }

}