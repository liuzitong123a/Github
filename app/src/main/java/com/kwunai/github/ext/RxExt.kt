package com.kwunai.github.ext

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.kwunai.github.common.GithubViewModel
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable


fun <T> Observable<T>.bindLifecycle(viewModel: GithubViewModel): ObservableSubscribeProxy<T> {
    return if (viewModel is LifecycleOwner) {
        `as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(viewModel, Lifecycle.Event.ON_DESTROY)))
    } else {
        throw throwableWhenLifecycleOwnerIsNull(viewModel)
    }
}

private fun throwableWhenLifecycleOwnerIsNull(viewModel: GithubViewModel): NullPointerException =
        NullPointerException("$viewModel's is not LifecycleOwner.")