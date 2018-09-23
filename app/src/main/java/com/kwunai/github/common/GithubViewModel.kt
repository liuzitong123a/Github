package com.kwunai.github.common

import android.arch.lifecycle.*
import android.support.annotation.CallSuper
import org.jetbrains.annotations.NotNull


open class GithubViewModel : ViewModel(), IViewModel {


    var lifecycleOwner: LifecycleOwner? = null

    @CallSuper
    override fun onCreate(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    @CallSuper
    override fun onStart(lifecycleOwner: LifecycleOwner) {

    }

    @CallSuper
    override fun onResume(lifecycleOwner: LifecycleOwner) {

    }

    @CallSuper
    override fun onPause(lifecycleOwner: LifecycleOwner) {

    }

    @CallSuper
    override fun onStop(lifecycleOwner: LifecycleOwner) {

    }

    @CallSuper
    override fun onDestroy(@NotNull lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = null
    }

}