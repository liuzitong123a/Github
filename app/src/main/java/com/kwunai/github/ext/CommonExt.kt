package com.kwunai.github.ext

import android.content.Context
import android.os.Bundle

import android.support.v4.app.Fragment

import android.support.v7.app.AppCompatActivity
import android.widget.Toast


fun AppCompatActivity.showFragment(containerId: Int, clazz: Class<out Fragment>, args: Bundle) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, clazz.newInstance().apply { arguments = args }, clazz.name)
            .commitAllowingStateLoss()
}


fun Context.toast(message: String?): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }

fun Fragment.toast(message: String?): Toast? = activity?.toast(message)

