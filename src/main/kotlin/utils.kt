import java.io.File

fun search(file: File, handler: (File) -> (Unit)) {
    file.listFiles().forEach {
        if (it.isDirectory) {
            search(it, handler)
        } else {
            handler(it)
        }
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
