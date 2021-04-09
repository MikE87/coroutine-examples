import kotlinx.coroutines.*

fun main() {
    printThreadNameFor("Main")

    runBlocking {
        launch {
            printThreadNameFor("None")
        }
        launch {
            withContext(Dispatchers.Default) {
                printThreadNameFor("with context")
            }
        }
        launch(Dispatchers.Default) {
            printThreadNameFor("Default")
        }
        launch(Dispatchers.IO) {
            printThreadNameFor("IO")
        }
        launch(Dispatchers.Unconfined) {
            printThreadNameFor("Unconfined")
        }
        launch(newSingleThreadContext("SingleThread")) {
            printThreadNameFor("SingleThread")
        }
    }
}

fun printThreadNameFor(context: String) {
    print("$context -".padEnd(15, ' '))
    println(Thread.currentThread().name)
}
