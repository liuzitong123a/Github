package com.kwunai.repo

import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.entity.AuthorizationReq
import com.kwunai.github.entity.Resource
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.http.api.AuthService
import com.kwunai.github.http.api.UserService
import com.kwunai.github.http.error.TokenInvalidException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response

/**
 * 用户发生改变的回调
 */
interface OnAuthStateChangeListener {

    fun onLogin(user: UserRsp)

    fun onLogout()
}

/**
 * 用户管理类
 */
class AuthRepository(
        private val userService: UserService,
        private val authService: AuthService,
        private val helper: PrefsHelper
) {

    val onAuthStateChangeListeners = ArrayList<OnAuthStateChangeListener>()

    private fun notifyLogin(user: UserRsp) {
        onAuthStateChangeListeners.forEach {
            it.onLogin(user)
        }
    }

    private fun notifyLogout() {
        onAuthStateChangeListeners.forEach { it.onLogout() }
    }

    fun setUserName(userName: String) {
        helper.username = userName
    }

    fun setPassword(password: String) {
        helper.password = password
    }

    fun getUserName(): String = helper.username

    fun getPassword(): String = helper.password

    fun login(): Observable<Resource<UserRsp>> {
        return authService.createAuthorization(AuthorizationReq())
                .subscribeOn(Schedulers.io())
                .doOnNext { if (it.token.isEmpty()) throw TokenInvalidException(it.id) }
                .retryWhen { it ->
                    it.flatMap {
                        if (it is TokenInvalidException) {
                            authService.deleteAuthorization(it.authId)
                        } else {
                            Observable.error(it)
                        }
                    }
                }
                .flatMap {
                    helper.token = it.token
                    helper.authId = it.id
                    userService.getAuthenticatedUser()
                }
                .map {
                    helper.user = it
                    notifyLogin(it)
                    return@map Resource.success(it)
                }
    }

    fun logout(): Observable<Resource<Boolean>> {
        return authService.deleteAuthorization(helper.authId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    helper.authId = -1
                    helper.token = ""
                    helper.user = null
                    notifyLogout()
                    return@map Resource.success(true)
                }
    }

}