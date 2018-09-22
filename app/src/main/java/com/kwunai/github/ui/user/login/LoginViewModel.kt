package com.kwunai.github.ui.user.login

import android.arch.lifecycle.MutableLiveData
import com.kwunai.github.common.GithubViewModel
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.entity.AuthorizationReq
import com.kwunai.github.entity.AuthorizationRsp
import com.kwunai.github.entity.Resource
import com.kwunai.github.entity.Status
import com.kwunai.github.ext.*
import com.kwunai.github.http.api.AuthService
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
        private val helper: PrefsHelper,
        private val webService: AuthService
) : GithubViewModel() {

    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val authorizationRsp: MutableLiveData<AuthorizationRsp> = MutableLiveData()

    init {
        username.value = helper.username
        password.value = helper.password
    }

    fun login() {

        checkEmail(username.value).no {
            error.value = IllegalArgumentException("邮箱格式不正确")
            return@login
        }

        checkPassword(password.value).yes {
            error.value = IllegalArgumentException("密码至少需要6位")
            return@login
        }

        helper.username = username.value ?: ""
        helper.password = password.value ?: ""
        webService.createAuthorization(AuthorizationReq())
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it.data) }
                .startWith(Resource.loading())
                .onErrorReturn { e -> Resource.error(e) }
                .bindLifecycle(this)
                .subscribe {
                    when (it.status) {
                        Status.LOADING -> loading.postValue(true)
                        Status.SUCCESS -> {
                            helper.isLoggedIn = true
                            helper.token = it.data!!.token
                            loading.postValue(false)
                            authorizationRsp.postValue(it.data)
                        }
                        Status.ERROR -> {
                            loading.postValue(false)
                            error.postValue(it.throwable)
                        }
                    }
                }
    }
}