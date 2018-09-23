package com.kwunai.github.ui.main.repo

import android.arch.lifecycle.LifecycleOwner
import android.util.Log
import com.kwunai.github.common.GithubViewModel
import com.kwunai.github.entity.GitHubPagingError
import com.kwunai.github.entity.GitHubPagingSuccess
import com.kwunai.github.entity.SearchRepo
import com.kwunai.github.ext.bindLifecycle
import com.kwunai.github.repo.RepoRepository

class RepoViewModel(
        private val repoRepository: RepoRepository
) : GithubViewModel() {


    override fun onCreate(lifecycleOwner: LifecycleOwner) {
        super.onCreate(lifecycleOwner)
        searchRepo()
    }

    private fun searchRepo() {
        repoRepository.searchRepo()
                .bindLifecycle(this)
                .subscribe {
                    when (it) {
                        is GitHubPagingSuccess -> {
                            val body: SearchRepo = it.body
                            body.nextPage = it.nextPage
                        }
                        is GitHubPagingError -> {
                            it.errorMessage
                        }
                    }
                }
    }

}