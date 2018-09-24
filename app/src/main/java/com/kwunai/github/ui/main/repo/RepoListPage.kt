package com.kwunai.github.ui.main.repo

import com.kwunai.github.entity.Repository
import com.kwunai.github.ext.format
import com.kwunai.github.http.api.RepoService
import com.kwunai.github.repo.ListPage
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.GitHubPaging
import java.util.*


class RepoListPage(
        private val owner: String? = null,
        private val repoService: RepoService
) : ListPage<Repository>() {
    override fun getData(page: Int): Observable<GitHubPaging<Repository>> {
        return if (owner == null) {
            repoService.searchRepo(page = page, q = "pushed:<" + Date()
                    .format("yyyy-MM-dd"))
                    .subscribeOn(Schedulers.io())
                    .map { it.paging }
        } else {
            repoService.userRepo(page = page, owner = owner)
        }

    }
}