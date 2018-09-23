package com.kwunai.github.repo

import com.kwunai.github.entity.*
import com.kwunai.github.ext.format
import com.kwunai.github.http.api.RepoService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*


class RepoRepository(
        private val repoService: RepoService
) {

    fun searchRepo(page: Int = 0): Observable<GitHubPaging<SearchRepo>> {
        return repoService.searchRepo(page = page, q = "pushed:<" + Date()
                .format("yyyy-MM-dd"))
                .subscribeOn(Schedulers.io())
                .map { GitHubPaging.create(it) }
                .onErrorReturn { GitHubPaging.create(it) }
    }

}