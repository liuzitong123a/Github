package com.kwunai.github.ext

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.kwunai.github.common.GithubViewModel


fun GithubViewModel.addLifecycle(activity: FragmentActivity) {
    activity.lifecycle.addObserver(this)
}

fun GithubViewModel.addLifecycle(fragment: Fragment) {
    fragment.lifecycle.addObserver(this)
}