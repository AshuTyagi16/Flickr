package com.sasuke.flickr.di.module.libraries

import android.content.Context
import com.sasuke.flickr.di.scope.FlickrAppScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PicassoModule {

    @Provides
    @FlickrAppScope
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
            .build()
    }
}