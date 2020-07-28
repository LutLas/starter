import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.File
import java.io.FileNotFoundException


fun main(args: Array<String>) {

    exampleOf("CompositeDisposable") {
        // 1
        val subscriptions = CompositeDisposable()
        // 2
        val disposable = Observable.just<String>("A", "B", "C")
                .subscribe {
                    // 3
                    //println(it)
                }
        // 4
        subscriptions.add(disposable)
    }

    exampleOf("create") {

        val disposables = CompositeDisposable()

        val observableDisposable = Observable.create<String> { emitter ->
            // 1
            emitter.onNext("1")

            // 2
            //emitter.onError(RuntimeException("Error"))
            //emitter.onComplete()

            // 3
            emitter.onNext("?")
        }.subscribeBy(
                onNext = { /*println(it)*/ },
                onComplete = { println("Completed") },
                onError = { println(it) }
        )

        disposables.add(observableDisposable)
        disposables.dispose()
    }

    exampleOf("defer") {

        val disposables = CompositeDisposable()
        // 1
        var flip = false
        // 2
        val factory: Observable<Int> = Observable.defer {
            // 3
            flip = !flip
            // 4
            if (flip) {
                Observable.just(1, 2, 3)
            } else {
                Observable.just(4, 5, 6)
            }
        }

        for (i in 0..3) {
            disposables.add(
                    factory.subscribe {
                        //println(it)
                    }
            )
        }

        disposables.dispose()
    }

    exampleOf("Single") {
        // 1
        val subscriptions = CompositeDisposable()
        // 2
        fun loadText(filename: String): Single<String> {
            // 3
            return Single.create create@{ emitter ->
                // 1
                val file = File(filename)
// 2
                if (!file.exists()) {
                    emitter.onError(FileNotFoundException("Can’t find $filename"))
                    return@create
                }
// 3
                val contents = file.readText(Charsets.UTF_8)
// 4
                emitter.onSuccess(contents)
            }
        }

        // 1
        val observer = loadText("Copyright.txt")
                // 2
                .subscribeBy(
                        // 3
                        onSuccess = { /*println(it)*/ },
                        onError = { /*println("Error, $it")*/ }
                )

        subscriptions.add(observer)
        subscriptions.dispose()
    }

    exampleOf("never") {
        val disposable = CompositeDisposable()

        val observable = Observable.never<Any>()
        val subscription = observable
                .doOnDispose { println("Disposed") }
                .doOnNext { println("Next Event: $it") }
                .doOnComplete { println("Completed") }
                .doOnError { println("Error Occurred: $it") }
                .doOnSubscribe { println("Subscribed: $it") }

        disposable.add(subscription.subscribe())
        disposable.dispose()
    }
}

