package tasks

import hashed
import readFromConsole
import repeatUntil
import search
import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.Exception

object Task3 {

    private const val defaultFolder = "tests/task3"
    private const val defaultFileName = "data.txt"
    private var currentUser: Pair<String, String>? = null

    fun run() {
        println("Task 3:")

        while (true) {
            if (currentUser == null) {
                println("1 - register")
                println("2 - sign in")
            }
            if (currentUser != null) {
                println("3 - get data")
                println("4 - set data")
                println("5 - logout")
            }

            val key = readFromConsole {
                x -> x.length == 1 && x.first().isDigit()
            }
            if (currentUser == null) {
                print("login: ")
                val login = readFromConsole()
                print("password: ")
                val password = readFromConsole {
                    x -> x.length >= 8
                }

                val currentUserFilePath = "$defaultFolder/${(login + password).hashed()}"
                when (key) {
                    "1" -> {
                        if (File(currentUserFilePath).exists()) {
                            println("User already registered")
                        } else {
                            register(login, password)
                        }
                    }
                    "2" -> {
                        if (!File(currentUserFilePath).exists()) {
                            println("User not found")
                        } else {
                            login(login, password)
                        }
                    }
                    else -> println("Invalid key")
                }
            } else {
                val currentUserFilePath = "$defaultFolder/${(currentUser!!.first + currentUser!!.second).hashed()}/$defaultFileName"
                when (key) {
                    "3" -> {
                        println("your data:")
                        println("\"${getFileContent(currentUserFilePath)}\"")
                    }
                    "4" -> {
                        println("enter data: ")
                        setFileContent(
                                currentUserFilePath,
                                readFromConsole {
                                    x -> x.isNotEmpty()
                                }
                        )
                    }
                    "5" -> {
                        logout()
                    }
                    else -> println("Invalid key")
                }
            }
        }
    }

    private fun register(login: String, password: String) {
        val folder = File("$defaultFolder/${(login + password).hashed()}")
        folder.mkdirs()

        File(folder, defaultFileName).createNewFile()
    }

    private fun login(login: String, password: String) {
        try {
            cipher(login, password, true)
        } catch (ex: Exception) {
            println(ex.message)
            return
        }
        currentUser = login to password
    }

    private fun logout() {
        cipher(currentUser!!.first, currentUser!!.second)
        currentUser = null
    }

    private fun cipher(login: String, password: String, decode: Boolean = false) {
        val userFiles = mutableListOf<File>()
        search(File("$defaultFolder/${(login + password).hashed()}")) {
            userFiles.add(it)
        }

        val cipher = Cipher.getInstance("AES")
        val aesKey = SecretKeySpec(
                password
                        .repeatUntil(32)
                        .toByteArray(),
                "AES"
        )

        cipher.init(
                if (decode) {
                    Cipher.DECRYPT_MODE
                } else {
                    Cipher.ENCRYPT_MODE
                },
                aesKey
        )

        userFiles.forEach { file ->
            if (decode) {
                val text = file.readText()
                if ("([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?".toRegex().find(text) == null) {    // regex check if data is base64 encoded
                    throw Exception("Your text is not encoded with Base64! Someone changed it!")
                }

                val decoded = Base64
                        .getDecoder()
                        .decode(text.trim()
                                .toByteArray()
                        )

                val decrypted: String
                try {
                    decrypted = String(
                            cipher.doFinal(decoded),
                            Charsets.UTF_8
                    )
                } catch (ex: Exception) {
                    throw Exception("Your text is not encrypted! Someone changed it!")
                }

                file.printWriter().use {
                    it.print(decrypted)
                }
            } else {
                val encrypted = StringBuilder()

                Base64.getEncoder()
                        .encode(cipher.doFinal(file.readText().toByteArray()))
                        .forEach {
                            encrypted.append(it.toChar())
                        }

                file.printWriter().use {
                    it.print(encrypted.toString())
                }
            }
        }
    }

    private fun setFileContent(path: String, data: String) =
            File(path)
                    .printWriter()
                    .use {
                        it.print(data)
                    }

    private fun getFileContent(path: String) = File(path).readText()

}