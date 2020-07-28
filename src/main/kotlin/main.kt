import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotlin.math.pow
import kotlin.math.roundToInt

fun main(args: Array<String>) {

    exampleOf("just") {
        val observable = Observable.just(listOf(1))
        observable.subscribe { println(it)}
    }

    exampleOf("fromIterable") {
        val observable: Observable<Int> =
                Observable.fromIterable(listOf(1, 2, 3))
        observable.subscribe { println(it)}
    }

    exampleOf("subscribe") {
        val observable = Observable.just(1, 2, 3)
        observable.subscribe { println(it) }
    }

    exampleOf("empty") {
        val observable = Observable.empty<Unit>()
        observable.subscribeBy(
                // 1
                onNext = { println(it) },
                // 2
                onComplete = { println("Completed") }
        )
    }

    exampleOf("never") {
        val observable = Observable.never<Any>()

        observable.subscribeBy(
                onNext = { println(it) },
                onComplete = { println("Completed") }
        )
    }

    exampleOf("range") {
        // 1
        val observable: Observable<Int> = Observable.range(1, 10)

        observable.subscribe {
            // 2
            val n = it.toDouble()
            val fibonacci = ((1.61803.pow(n) - 0.61803.pow(n)) /
                    2.23606).roundToInt()
            println(fibonacci)
        }
    }

    exampleOf("dispose") {
        // 1
        val mostPopular: Observable<String> = Observable.just("A", "B", "C")
        // 2
        val subscription = mostPopular.subscribe {
            // 3
            println(it)
        }
    }

}

