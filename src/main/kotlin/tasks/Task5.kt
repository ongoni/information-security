package tasks

import random
import readFromConsole
import java.io.File

object Task5 {

    private const val defaultFolderPath = "tests/task5"
    private const val defaultFileName = "default.txt"
    private const val defaultFileFullPath = "$defaultFolderPath/$defaultFileName"
    private val defaultAlphabet = listOf(
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
    )

    fun run(text: String) {
        println("Task 5:")
        val index = (1..defaultAlphabet.lastIndex).random()
        print("Enter keyword: ")
        val keyWord = readFromConsole {
            x -> !x.isEmpty() && x.length < 33 && x.length > 1
        }

        val keyWordSymbols = keyWord
                .toList()
                .sorted()
        val encodeMap = mutableMapOf<Char, Char>()

        for (i: Int in (index .. index + keyWordSymbols.lastIndex)) {
            encodeMap[defaultAlphabet[i % defaultAlphabet.size]] = keyWordSymbols[i - index]
        }

        val notUsed = defaultAlphabet.minus(keyWordSymbols)
        for ((j, i: Int) in (index + keyWordSymbols.size until index + keyWordSymbols.size + notUsed.size).withIndex()) {
            encodeMap[defaultAlphabet[i % defaultAlphabet.size]] = notUsed[j]
        }

        preprocess()

        encode(text, encodeMap)
        println(decode(encodeMap))
    }

    private fun encode(text: String, encodeMap: MutableMap<Char, Char>) {
        val toEncode = text
                .filter { x -> x.isLetter() || x == ' ' }
                .map { it.toLowerCase() }

        File("$defaultFolderPath/$defaultFileName").printWriter().use { writer ->
            writer.print(
                    toEncode
                            .asSequence()
                            .map {
                                if (it == ' ') {
                                    ' '
                                } else {
                                    encodeMap[it]
                                }
                            }
                            .joinToString("")
            )
        }
    }

    private fun decode(encodeMap: MutableMap<Char, Char>) =
            File(defaultFileFullPath)
                    .readText()
                    .map {
                        if (it == ' ') {
                            ' '
                        } else {
                            encodeMap.filter {
                                x -> x.value == it
                            }.toList().first().first
                        }
                    }
                    .joinToString("")

    private fun preprocess() {
        if (!File(defaultFileFullPath).exists()) {
            val folder = File(defaultFolderPath)
            folder.mkdirs()
            val file = File(folder, defaultFileName)
            file.createNewFile()
        }
    }

}