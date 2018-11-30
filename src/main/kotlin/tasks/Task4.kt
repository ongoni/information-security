package tasks

import toBitString
import java.io.File
import java.lang.Byte
import kotlin.text.Typography.nbsp

object Task4 {

    private const val defaultFile = "tests/task4/lorem-ipsum.txt"

    fun run(text: String = "something really secret") {
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

        var words = fileText
                .split(" ")
                .toMutableList()
        val bits = textToEncode
                .toByteArray()
                .toBitString()

        if (!words.isEmpty()) {
            var bitIndex = 0
            words = words.map {
                if (bitIndex < bits.length && bits[bitIndex] == '1') {
                    bitIndex++
                    "$it$nbsp"
                } else {
                    bitIndex++
                    "$it "
                }
            }.toMutableList()

            file.printWriter().use {
                it.print(words.joinToString(""))
            }
        }
    }

    private fun decode(): String {
        val file = File(defaultFile)
        val text = file.readText()
        val bits = StringBuilder()

        text.forEach {
            if (it == ' ') {
                bits.append('0')
            }
            if (it == nbsp) {
                bits.append('1')
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

    private fun isEncoded(text: String): Boolean = text.indexOf(nbsp) != -1

}