package com.kwunai.github.ui.login

import com.kwunai.github.ext.addLifecycle
import com.kwunai.github.repo.AuthRepository
import org.kodein.di.Kodein
import org.kodein.di.android.AndroidComponentsWeakScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton


const val LOGIN_MODULE_TAG = "LOGIN_MODULE_TAG"

val loginModule = Kodein.Module(LOGIN_MODULE_TAG) {

    bind<AuthRepository>() with scoped(AndroidComponentsWeakScope).singleton {
        AuthRepository(instance(), instance(), instance())
    }

    bind<LoginViewModel>() with scoped(AndroidComponentsWeakScope).singleton {
        LoginViewModel(instance()).apply {
            addLifecycle(instance<LoginActivity>())
        }
    }
}