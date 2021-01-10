package com.catsnews.di.component

import com.catsnews.di.module.ContextModule
import com.catsnews.di.module.DataModule
import com.catsnews.di.module.DomainModule
import com.catsnews.di.module.PresentationModule
import com.catsnews.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {

    fun injectMainActivity(mainActivity: MainActivity)

}