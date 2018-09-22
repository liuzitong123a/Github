package com.kwunai.github.http.api

import com.kwunai.github.entity.UserRsp
import io.reactivex.Observable
import retrofit2.http.GET


interface UserService {

    @GET("/user")
    fun getAuthenticatedUser(): Observable<UserRsp>

}