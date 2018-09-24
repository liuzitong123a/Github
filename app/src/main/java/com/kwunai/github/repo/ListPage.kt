package com.kwunai.github.repo

import io.reactivex.Observable
import retrofit2.adapter.rxjava2.GitHubPaging

/**
 * 下拉加载上拉刷新
 * @author lzt
 */
abstract class ListPage<DataType> : DataProvider<DataType> {

    var currentPage = 1
        private set

    val data = GitHubPaging<DataType>()

    fun refresh(pageCount: Int = currentPage): Observable<GitHubPaging<DataType>> =
            Observable.range(1, pageCount)
                    .concatMap { getData(it) }
                    .reduce { acc, page -> acc.mergeData(page) }
                    .toObservable()
                    .doOnNext {
                        data.clear()
                        data.mergeData(it)
                    }

    fun loadMore(): Observable<GitHubPaging<DataType>> = getData(currentPage + 1)
            .doOnNext { currentPage + 1 }
            .map {
                data.mergeData(it)
                return@map data
            }
}