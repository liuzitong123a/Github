package com.kwunai.github.di

import com.kwunai.github.http.api.AuthService
import com.kwunai.github.http.api.RepoService
import com.kwunai.github.http.api.UserService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit


const val SERVICE_MODULE_TAG = "serviceModule"

val serviceModule = Kodein.Module(SERVICE_MODULE_TAG) {

    bind<AuthService>() with singleton {
        instance<Retrofit>().create(AuthService::class.java)
    }

    bind<UserService>() with singleton {
        instance<Retrofit>().create(UserService::class.java)
    }

    bind<RepoService>() with singleton {
        instance<Retrofit>().create(RepoService::class.java)
    }
}