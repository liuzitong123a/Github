package com.kwunai.github.ext

import android.arch.lifecycle.LifecycleOwner
import com.kwunai.github.common.GithubViewModel
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.bindLifecycle(lifecycleOwner: LifecycleOwner): ObservableSubscribeProxy<T> =
        `as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))

fun <T> Observable<T>.bindLifecycle(viewModel: GithubViewModel): ObservableSubscribeProxy<T> =
        bindLifecycle(viewModel.lifecycleOwner
                ?: throw throwableWhenLifecycleOwnerIsNull(viewModel))


fun LifecycleOwner.delayTimer(timer: Long, onNext: () -> Unit) {
    Observable.timer(timer, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLifecycle(lifecycleOwner = this)
            .subscribe { onNext() }
}

private fun throwableWhenLifecycleOwnerIsNull(viewModel: GithubViewModel): NullPointerException =
        NullPointerException("$viewModel's is not LifecycleOwner.")