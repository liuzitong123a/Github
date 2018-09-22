package com.kwunai.github.ui.login

import android.arch.lifecycle.MutableLiveData
import com.kwunai.github.common.GithubViewModel
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.entity.AuthorizationReq
import com.kwunai.github.entity.Resource
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.ext.*
import com.kwunai.github.http.api.AuthService
import com.kwunai.github.http.api.UserService
import com.kwunai.github.http.error.TokenInvalidException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
        private val helper: PrefsHelper,
        private val authService: AuthService,
        private val userService: UserService
) : GithubViewModel() {

    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val user: MutableLiveData<UserRsp> = MutableLiveData()

    init {
        username.value = helper.username
        password.value = helper.password
    }

    fun login() {

        checkEmail(username.value).yes {
            error.value = IllegalArgumentException("邮箱格式不正确")
            return@login
        }

        checkPassword(password.value).yes {
            error.value = IllegalArgumentException("密码至少需要6位")
            return@login
        }

        helper.username = username.value ?: ""
        helper.password = password.value ?: ""
        authService.createAuthorization(AuthorizationReq())
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
                .map { Resource.success(it) }
                .startWith(Resource.loading())
                .onErrorReturn { e -> Resource.error(e) }
                .bindLifecycle(viewModel = this)
                .subscribe {
                    when (it) {
                        is Resource.Loading -> loading.postValue(true)
                        is Resource.Success -> {
                            helper.user = it.result
                            loading.postValue(false)
                            user.postValue(it.result)
                        }
                        is Resource.Error -> {
                            loading.postValue(false)
                            error.postValue(it.error)
                        }
                    }
                }
    }
}