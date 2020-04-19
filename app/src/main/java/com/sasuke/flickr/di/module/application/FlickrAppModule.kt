package com.sasuke.flickr.di.module.application

import com.sasuke.flickr.di.scope.FlickrAppScope
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class FlickrAppModule {

    @Provides
    @FlickrAppScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }
}