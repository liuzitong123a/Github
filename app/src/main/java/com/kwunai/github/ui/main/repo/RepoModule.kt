package com.kwunai.github.ui.main.repo

import com.kwunai.github.ext.addLifecycle
import com.kwunai.github.repo.RepoRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

const val HOME_MODULE_TAG = "REPO_MODULE_TAG"

val repoModule = Kodein.Module(HOME_MODULE_TAG) {

    bind<RepoViewModel>() with provider {
        RepoViewModel(instance()).apply {
            addLifecycle(instance<RepoFragment>())
        }
    }

    bind<RepoRepository>() with provider {
        RepoRepository(instance())
    }

}


