import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KProperty
import kotlin.system.measureTimeMillis

val atomicCounter = AtomicInteger()
var noSyncCounter = 0
var mutexCounter = 0
var fineGrainedCounter = 0
var coarseGrainedCounter = 0

val confinedContext = newSingleThreadContext("confined")
val mutex = Mutex()

fun main() = runBlocking {
    withContext(Default) {

        compute(::noSyncCounter) {
           noSyncCounter++
        }

        compute(::atomicCounter, atomicCounter::incrementAndGet)

        compute(::mutexCounter) {
            mutex.withLock { mutexCounter++ }
        }

        compute(::fineGrainedCounter) {
            withContext(confinedContext) { fineGrainedCounter++ }
        }

        withContext(confinedContext) {
            compute(::coarseGrainedCounter) {
                coarseGrainedCounter++
            }
        }
    }
}

suspend fun compute(counter: KProperty<Any>, action: suspend () -> Unit) {
    printHeader(counter.name)
    val time = measureTimeMillis {
        coroutineScope {
            repeat(100) {
                launch {
                    repeat(1000) { action() }
                }
            }
        }
    }
    println("Completed 100000 actions in $time ms")
    println("Counter = ${counter.call()}")
}

fun printHeader(name: String) {
    println(
        "\u001B[7m     "
            .plus(name)
            .padEnd(40, ' ')
            .plus("\u001B[m")
    )
}
