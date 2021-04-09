import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main() = runBlocking {
    val progress = launch(start = CoroutineStart.LAZY) {
        progressInfo()
    }

    val request = launch(IO) {
        withTimeout(2000L) {
            fetch(progress)
        }
    }
    request.invokeOnCompletion {
        progress.cancel()
        it?.let {
            println("Fuck this shit, abort!")
            println(it)
        }
    }

    println("### Random Number Fetch ###")

    delay(1000L)
    request.cancel("Manual cancel")
}

suspend fun fetch(progress: Job) = runInterruptible {
    val request = HttpRequest
        .newBuilder(URI.create("http://localhost:8080/number"))
        .GET()
        .build()

    progress.start()

    val response = HttpClient
        .newHttpClient()
        .send(request, HttpResponse.BodyHandlers.ofString())

    println(response.body())
}

suspend fun progressInfo() {
    print("And the number is")
    while(true) {
        delay(250L)
        print(".")
    }
}
