package com.kwunai.github.ui.main.repo.search

import com.kwunai.github.entity.Repository
import com.kwunai.github.ext.format
import com.kwunai.github.http.api.RepoService
import com.kwunai.github.repo.ListPage
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.GitHubPaging
import java.util.*


class SearchRepoListPage(
        private val repoService: RepoService
) : ListPage<Repository>() {
    override fun getData(page: Int): Observable<GitHubPaging<Repository>> {
        return repoService.searchRepo(page = page, q = "pushed:<" + Date()
                .format("yyyy-MM-dd"))
                .subscribeOn(Schedulers.io())
                .map { it.paging }
    }
}