package com.kwunai.github.ui.main

import android.arch.lifecycle.MutableLiveData
import com.kwunai.github.common.GithubViewModel
import com.kwunai.github.entity.Resource
import com.kwunai.github.ext.bindLifecycle
import com.kwunai.repo.AuthRepository


class MainViewModal(
        private val authRepository: AuthRepository
) : GithubViewModel() {

    val error: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val logout: MutableLiveData<Boolean> = MutableLiveData()

    fun logout() {
        authRepository.logout()
                .startWith(Resource.loading())
                .onErrorReturn { e -> Resource.error(e) }
                .bindLifecycle(this)
                .subscribe {
                    when (it) {
                        is Resource.Loading -> loading.value = true
                        is Resource.Success -> {
                            logout.value = true
                            loading.value = false
                        }
                        is Resource.Error -> {
                            loading.value = false
                            error.value = it.error
                        }
                    }
                }
    }

}