import kotlinx.coroutines.*

val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
}

fun main() = runBlocking {
    supervisorScope {
        println("Supervisor - let other coroutines finish")
        launch(handler) {
            throw AssertionError()
        }
        val deferred = async {
            delay(100L)
            "adsf"
        }

        println("Got async result: ${deferred.await()}")
    }

    val job = GlobalScope.launch(handler) {
        println("Cancel everything on single coroutine fail")
        launch {
            throw AssertionError("Not gonna get the async result")
        }
        val deferred = async {
            delay(100L)
            "adsf"
        }

        println("Got async result: ${deferred.await()}")
    }
    job.join()
}
