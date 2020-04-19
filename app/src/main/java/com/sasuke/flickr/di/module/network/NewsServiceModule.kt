package com.sasuke.flickr.di.module.network

import com.sasuke.flickr.data.network.FlickrService
import com.sasuke.flickr.di.scope.FlickrAppScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class NewsServiceModule {

    companion object {
        private const val BASE_URL_RELEASE = "https://api.flickr.com/services/"
        private const val BASE_URL = BASE_URL_RELEASE
    }

    @Provides
    @FlickrAppScope
    fun instaSolvService(retrofit: Retrofit): FlickrService {
        return retrofit.create(FlickrService::class.java)
    }

    @Provides
    @FlickrAppScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @FlickrAppScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }
}