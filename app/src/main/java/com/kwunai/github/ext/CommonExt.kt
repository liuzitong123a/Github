package com.kwunai.github.ext

import android.os.Bundle

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.showFragment(containerId: Int, clazz: Class<out Fragment>, args: Bundle) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, clazz.newInstance().apply { arguments = args }, clazz.name)
            .commitAllowingStateLoss()
}

