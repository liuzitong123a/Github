package com.kwunai.github.binding

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


interface ViewClickConsumer : Consumer<View>

const val DEFAULT_THROTTLE_TIME = 500L


/**
 * [View]是否可见
 *
 * @param visible 值为true时可见
 */
@BindingAdapter("bind_visibility")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * [View]防抖动点击事件
 *
 * @param consumer 点击事件消费者
 * @param time 防抖动时间间隔，单位ms
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_onClick", "bind_throttleFirst", requireAll = false)
fun setOnClickEvent(view: View, consumer: ViewClickConsumer, time: Long?) {
    RxView.clicks(view)
            .throttleFirst(time ?: DEFAULT_THROTTLE_TIME, TimeUnit.MILLISECONDS)
            .subscribe { consumer.accept(view) }
}
