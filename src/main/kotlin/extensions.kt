import java.security.MessageDigest
import java.util.*

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start

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

fun ByteArray.toHexString(): String {
    val hexChars = "0123456789abcdef"
    val result = StringBuilder(this.size * 2)

    this.forEach {
        val i = it.toInt()
        result.append(hexChars[i shr 4 and 0x0f])
        result.append(hexChars[i and 0x0f])
    }

    return result.toString()
}

fun String.repeatUntil(length: Int = 32) =
        this
                .repeat(length / this.length + 1)
                .substring((0 until length))

fun String.hashed(type: String = "SHA-512"): String = hash(type, this.toByteArray())

private fun hash(type: String, bytes: ByteArray): String =
        MessageDigest
                .getInstance(type)
                .digest(bytes)
                .toHexString()
