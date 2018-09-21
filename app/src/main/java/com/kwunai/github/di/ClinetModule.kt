package com.kwunai.github.di

import com.google.gson.Gson
import com.kwunai.github.GithubConstant
import com.kwunai.github.http.interceptors.AcceptInterceptor
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val GITHUB_CLIENT_MODULE_TAG = "ClientModule"

const val GITHUB_INTERCEPTOR_LOG_TAG = "github_interceptor_log"
const val GITHUB_INTERCEPTOR_ACCEPT_TAG = "github_interceptor_accept"


val clientModule = Kodein.Module(GITHUB_CLIENT_MODULE_TAG) {

    bind<Retrofit.Builder>() with provider { Retrofit.Builder() }

    bind<OkHttpClient.Builder>() with provider { OkHttpClient.Builder() }


    bind<Interceptor>(GITHUB_INTERCEPTOR_LOG_TAG) with singleton {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Logger.json(it) })
                .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    bind<Interceptor>(GITHUB_INTERCEPTOR_ACCEPT_TAG) with singleton {
        AcceptInterceptor()
    }


    bind<OkHttpClient>() with singleton {
        instance<OkHttpClient.Builder>()
                .connectTimeout(GithubConstant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(GithubConstant.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(GithubConstant.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(instance(GITHUB_INTERCEPTOR_LOG_TAG))
                .addInterceptor(instance(GITHUB_INTERCEPTOR_ACCEPT_TAG))
                .build()
    }

    bind<Retrofit>() with singleton {
        instance<Retrofit.Builder>()
                .baseUrl(GithubConstant.BASE_URL)
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    bind<Gson>() with singleton { Gson() }

}