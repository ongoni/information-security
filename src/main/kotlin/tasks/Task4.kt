package tasks

import toBitString
import java.io.File
import java.lang.Byte

object Task4 {

    private const val defaultFile = "tests/task4/lorem-ipsum.txt"

    fun run(text: String = "secret message") {
        println("Task 4:")
        Task4.encode(text)
        println(Task4.decode())
    }

    private fun encode(textToEncode: String) {
        val file = File(defaultFile)
        val fileText = file.readText()

        if (isEncoded(fileText)) {
            println("File already have encoded data")
            return
        }

        val words = fileText
                .split(" ")
                .toMutableList()
        val bits = textToEncode
                .toByteArray()
                .toBitString()

        if (!words.isEmpty()) {
            bits.forEachIndexed { index, bit ->
                if (bit == '1') {
                    words[index] = "${words[index]} "
                }
            }

            file.printWriter().use {
                it.print(words.joinToString(" "))
            }
        }
    }

    private fun decode(): String {
        val file = File(defaultFile)
        val text = file.readText()
        val bits = StringBuilder()

        var whitespaceCount = 0
        for (index: Int in (0..text.lastIndex)) {
            if (text[index] == ' ') {
                whitespaceCount++
            }

            if (whitespaceCount == 1 && index + 1 != text.length && text[index + 1] != ' ') {
                bits.append('0')
                whitespaceCount = 0
            }

            if (whitespaceCount == 2) {
                bits.append('1')
                whitespaceCount = 0
            }
        }

        val bytes = bits
                .toString()
                .chunked(8)
                .asSequence()
                .filter { x -> x != "00000000" }
                .filter { x -> x.length == 8 }
                .map { Byte.parseByte(it, 2) }
                .toList()
                .toByteArray()

        return String(bytes, Charsets.UTF_8)
    }

    private fun isEncoded(text: String): Boolean = text.indexOf("  ") != -1

}