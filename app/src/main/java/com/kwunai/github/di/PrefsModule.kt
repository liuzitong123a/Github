package com.kwunai.github.di

import android.content.Context
import android.content.SharedPreferences
import com.kwunai.github.GithubApp
import com.kwunai.github.data.PrefsHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

const val PREFS_MODULE_TAG = "PrefsModule"

const val PREFS_DEFAULT_SP_TAG = "PrefsDefault"

val prefsModule = Kodein.Module(PREFS_MODULE_TAG) {

    bind<SharedPreferences>(PREFS_DEFAULT_SP_TAG) with singleton {
        GithubApp.INSTANCE.getSharedPreferences(PREFS_DEFAULT_SP_TAG, Context.MODE_PRIVATE)
    }

    bind<PrefsHelper>() with singleton {
        PrefsHelper(instance(PREFS_DEFAULT_SP_TAG))
    }
}