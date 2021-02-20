package com.example.currentweather.helper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class RecyclableCompositeDisposable @Inject constructor() {

    var compositeDisposable = CompositeDisposable()

    fun add(disposable: Disposable) = compositeDisposable.add(disposable)

    fun disposeAndReset() {
        compositeDisposable.let {
            compositeDisposable = CompositeDisposable()
            it.dispose()
        }
    }
}