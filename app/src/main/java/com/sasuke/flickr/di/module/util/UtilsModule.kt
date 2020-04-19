package com.sasuke.flickr.di.module.util

import android.content.Context
import com.sasuke.flickr.di.scope.FlickrAppScope
import com.sasuke.flickr.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    @FlickrAppScope
    fun networkUtil(context: Context): NetworkUtil {
        return NetworkUtil(context)
    }
}