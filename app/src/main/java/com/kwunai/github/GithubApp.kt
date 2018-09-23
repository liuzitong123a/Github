package com.kwunai.github

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.kwunai.github.di.clientModule
import com.kwunai.github.di.prefsModule
import com.kwunai.github.di.serviceModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class GithubApp : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@GithubApp }
        import(androidModule(this@GithubApp))
        import(androidSupportModule(this@GithubApp))

        import(clientModule)
        import(serviceModule)
        import(prefsModule)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Stetho.initializeWithDefaults(this)
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(0)
                .methodOffset(7)
                .showThreadInfo(false)
                .tag(GithubConstant.LOGGER_TAG)
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    companion object {
        lateinit var INSTANCE: GithubApp
    }

}