import java.util.*

fun ByteArray.toByteString() = this.joinToString(" ") {
    it.toString()
}

fun ByteArray.toBitString() = this.joinToString("") {
    val result = Integer.toBinaryString(Integer.parseInt(it.toString()))
    if (result.length < 8) {
        "${"0".repeat(8 - result.length)}$result"
    } else {
        result
    }
}

fun readFromConsole(condition: (String) -> (Boolean) = { !it.isEmpty() }): String {
    lateinit var text: String
    while (true) {
        text = readLine().toString()

        if (!condition(text)) {
            print("Incorrect data! Try again: ")
            continue
        }

        break
    }

    return text
}

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start
