package com.kwunai.github.repo

import com.kwunai.github.entity.GitHubPaging
import io.reactivex.Observable

interface DataProvider<DataType> {
    fun getData(page: Int): Observable<List<GitHubPaging<DataType>>>
}