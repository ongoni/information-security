import tasks.*

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Task1.run()
            Task2.run("png")
            Task3.run()
            Task4.run("some really secret text to hide")
            Task5.run("больше текста для шифрования")
        }
    }

}