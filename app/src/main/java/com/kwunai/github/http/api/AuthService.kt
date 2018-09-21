package com.kwunai.github.http.api

import com.kwunai.github.GithubConfig
import com.kwunai.github.entity.AuthorizationReq
import com.kwunai.github.entity.AuthorizationRsp
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthService {

    @PUT("/authorizations/clients/${GithubConfig.clientId}/{fingerPrint}")
    fun createAuthorization(@Body req: AuthorizationReq,
                            @Path("fingerPrint") fingerPrint: String = GithubConfig.fingerPrint): Observable<AuthorizationRsp>


    @DELETE("/authorizations/{id}")
    fun deleteAuthorization(@Path("id") id: Int): Observable<Response<Any>>

}