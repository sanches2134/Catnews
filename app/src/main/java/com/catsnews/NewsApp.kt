package com.catsnews

import android.app.Application
import com.catsnews.di.component.AppComponent
import com.catsnews.di.component.DaggerAppComponent
import com.catsnews.di.module.ContextModule


class NewsApp : Application() {
    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component =
            DaggerAppComponent.builder().contextModule(ContextModule(this.applicationContext))
                .build()
    }
}
