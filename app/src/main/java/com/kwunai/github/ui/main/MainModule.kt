package com.kwunai.github.ui.main

import com.kwunai.github.ext.addLifecycle
import com.kwunai.github.repo.AuthRepository
import org.kodein.di.Kodein
import org.kodein.di.android.AndroidComponentsWeakScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

const val MAIN_MODULE_TAG = "MAIN_MODULE_TAG"

val mainModule = Kodein.Module(MAIN_MODULE_TAG) {

    bind<AuthRepository>() with scoped(AndroidComponentsWeakScope).singleton {
        AuthRepository(instance(), instance(), instance())
    }

    bind<MainViewModal>() with scoped(AndroidComponentsWeakScope).singleton {
        MainViewModal(instance()).apply {
            addLifecycle(instance<MainActivity>())
        }
    }
}