package com.butterfly.klepto.tweetitsweet

import android.app.Application

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: BaseApplication? = null
            private set


    }

}