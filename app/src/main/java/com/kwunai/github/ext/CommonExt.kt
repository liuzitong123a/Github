package com.kwunai.github.ext

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast

fun Context.toast(message: String?): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }

fun Fragment.toast(message: String?): Toast? = activity?.toast(message)