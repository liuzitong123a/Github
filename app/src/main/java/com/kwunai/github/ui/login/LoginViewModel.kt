package com.kwunai.github.ui.login

import android.arch.lifecycle.MutableLiveData
import com.kwunai.github.common.GithubViewModel
import com.kwunai.github.entity.Resource
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.ext.*
import com.kwunai.repo.AuthRepository


class LoginViewModel(
        private val authRepository: AuthRepository
) : GithubViewModel() {

    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val user: MutableLiveData<UserRsp> = MutableLiveData()

    init {
        username.value = authRepository.getUserName()
        password.value = authRepository.getPassword()
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

        authRepository.setUserName(username.value ?: "")
        authRepository.setPassword(password.value ?: "")
        authRepository.login()
                .startWith(Resource.loading())
                .onErrorReturn { e -> Resource.error(e) }
                .bindLifecycle(viewModel = this)
                .subscribe {
                    when (it) {
                        is Resource.Loading -> loading.postValue(true)
                        is Resource.Success -> {
                            user.postValue(it.result)
                            loading.postValue(false)
                        }
                        is Resource.Error -> {
                            loading.postValue(false)
                            error.postValue(it.error)
                        }
                    }
                }
    }
}