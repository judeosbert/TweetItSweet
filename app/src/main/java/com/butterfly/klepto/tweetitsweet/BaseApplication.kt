package com.butterfly.klepto.tweetitsweet

import android.app.Application
import com.butterfly.klepto.tweetitsweet.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }

    companion object {
        var instance: BaseApplication? = null
            private set


    }

}