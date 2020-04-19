package com.sasuke.flickr.di.component

import android.content.Context
import com.sasuke.flickr.di.module.activity.ActivityBindingModule
import com.sasuke.flickr.di.module.application.FlickrAppModule
import com.sasuke.flickr.di.module.libraries.PicassoModule
import com.sasuke.flickr.di.module.network.NewsApiModule
import com.sasuke.flickr.di.module.util.UtilsModule
import com.sasuke.flickr.di.module.util.ViewModelFactoryModule
import com.sasuke.flickr.di.scope.FlickrAppScope
import com.google.gson.Gson
import com.sasuke.flickr.FlickrApp
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import timber.log.Timber

@FlickrAppScope
@Component(
    modules = [
        FlickrAppModule::class,
        NewsApiModule::class,
        PicassoModule::class,
        UtilsModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelFactoryModule::class,
        ActivityBindingModule::class
    ]
)
interface FlickrAppComponent : AndroidInjector<FlickrApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): FlickrAppComponent
    }

    fun context(): Context

    fun timberTree(): Timber.Tree

    fun getPicasso(): Picasso

    fun getGson(): Gson
}