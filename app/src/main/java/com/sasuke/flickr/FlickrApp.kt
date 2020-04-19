package com.sasuke.flickr

import android.app.Application
import com.sasuke.flickr.di.component.DaggerFlickrAppComponent
import com.sasuke.flickr.di.component.FlickrAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class FlickrApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var component: FlickrAppComponent

    override fun onCreate() {
        super.onCreate()
        initComponent()
        initTimber()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private fun initTimber() {
        Timber.plant(component.timberTree())
    }

    private fun initComponent() {
        component = DaggerFlickrAppComponent.factory().create(applicationContext)
        component.inject(this)
    }

    fun newsApplicationComponent(): FlickrAppComponent {
        return component
    }
}