import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy


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

    
}

