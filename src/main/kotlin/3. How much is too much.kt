import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main() {
//    val time = runALotOfCoroutines()
    val time = runALotOfThreads()

    println("Took $time ms.")
}

fun runALotOfCoroutines() = measureTimeMillis {
    runBlocking {
        repeat(100_000) {
            launch(Dispatchers.Default) {
                delay(1000L)
//                println("Coroutine: $it - ${Thread.currentThread().name}")
            }
        }
    }
}

fun runALotOfThreads() = measureTimeMillis {
    val threads = mutableListOf<Thread>()
    repeat(10_000) {
        thread {
            Thread.sleep(1000L)
//            println("Thread: $it - ${Thread.currentThread().name}")
        }.let(threads::add)
    }
    threads.forEach(Thread::join)
}
