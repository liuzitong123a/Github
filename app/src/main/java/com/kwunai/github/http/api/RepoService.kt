package com.kwunai.github.http.api

import com.kwunai.github.entity.GitHubPaging
import com.kwunai.github.entity.Repository
import com.kwunai.github.entity.SearchRepo
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RepoService {

    @GET("/users/{owner}/repos?type=all")
    fun listRepositoriesOfUser(@Path("owner") owner: String, @Query("page") page: Int = 1, @Query("per_page") per_page: Int = 20): Observable<GitHubPaging<Repository>>

    @GET("/search/repositories?order=desc&sort=updated")
    fun searchRepo(
            @Query("page") page: Int = 1,
            @Query("q") q: String,
            @Query("per_page") per_page: Int = 20
    ): Observable<Response<SearchRepo>>
}