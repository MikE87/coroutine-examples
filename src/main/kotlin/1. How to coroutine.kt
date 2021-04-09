import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val a = async {
            delay(1000L)
        }
        val b = async {
            delay(1000L)
        }

        print("Waiting for results...")
        a.await()
        b.await()
        println("Done")
    }

    println("Took: $time")
}
