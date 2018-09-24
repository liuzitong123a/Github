package com.kwunai.github.repo

import retrofit2.adapter.rxjava2.GitHubPaging
import io.reactivex.Observable

interface DataProvider<DataType> {
    fun getData(page: Int): Observable<GitHubPaging<DataType>>
}