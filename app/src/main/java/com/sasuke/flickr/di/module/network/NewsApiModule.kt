package com.sasuke.flickr.di.module.network

import com.sasuke.flickr.data.network.FlickrRepository
import com.sasuke.flickr.data.network.FlickrService
import com.sasuke.flickr.di.scope.FlickrAppScope
import dagger.Module
import dagger.Provides

@Module(includes = [NewsServiceModule::class])
class NewsApiModule {

    @Provides
    @FlickrAppScope
    fun instaSolveApi(flickrService: FlickrService): FlickrRepository {
        return FlickrRepository(flickrService)
    }
}