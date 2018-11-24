package tasks

import readFromConsole
import java.io.File
import java.lang.StringBuilder
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Task3 {

    private const val defaultFolder = "tests/task3"

    fun run() {
        println("Task 3:")
        print("Login: ")
        val login = readFromConsole()
        print("Password: ")
        val password = readFromConsole { x -> x.length >= 16 }
        print("Text: ")
        val text = readFromConsole()

        Task3.register(
                login,
                password,
                text
        )

        val userContent = Task3.getUserFileContent(login, password)
        println(userContent)
    }

    private fun register(login: String, password: String, text: String) {
        val folder = File(defaultFolder)
        folder.mkdirs()
        val file = File(folder, "$login.txt")
        file.createNewFile()

        val aesKey = SecretKeySpec(password.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")

        cipher.init(Cipher.ENCRYPT_MODE, aesKey)

        val encrypted = StringBuilder()
        Base64.getEncoder()
                .encode(cipher.doFinal(text.toByteArray()))
                .forEach {
                    encrypted.append(it.toChar())
                }

        file.printWriter().use {
            it.println(encrypted.toString())
        }
    }

    private fun getUserFileContent(login: String, password: String): String {
        val file = File("$defaultFolder/$login.txt")
        val text = file.readText()

        val aesKey = SecretKeySpec(password.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")

        cipher.init(Cipher.DECRYPT_MODE, aesKey)

        return String(
                cipher.doFinal(Base64
                        .getDecoder()
                        .decode(text.trim().toByteArray())
                ),
                Charsets.UTF_8
        )
    }

}