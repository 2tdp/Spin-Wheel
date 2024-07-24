package com.spin.wheel.chooser.wheeloffortune.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {

    protected var dispoable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        dispoable.clear()
    }
}