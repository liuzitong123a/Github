package com.kwunai.github.common

import android.arch.lifecycle.*
import android.support.annotation.CallSuper
import org.jetbrains.annotations.NotNull


open class GithubViewModel : ViewModel(), LifecycleObserver {

    var lifecycleOwner: LifecycleOwner? = null

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = null
    }

}