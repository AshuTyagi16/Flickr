package com.sasuke.flickr.di.module.network

import android.content.Context
import com.sasuke.flickr.data.exception.NoConnectivityException
import com.sasuke.flickr.di.qualifier.CacheInterceptor
import com.sasuke.flickr.di.qualifier.FlickrLoggingInterceptor
import com.sasuke.flickr.di.qualifier.FlickrNetworkInterceptor
import com.sasuke.flickr.di.qualifier.StaleIfErrorInterceptor
import com.sasuke.flickr.di.scope.FlickrAppScope
import com.sasuke.flickr.util.NetworkUtil
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    companion object {
        private const val CONNECTION_TIMEOUT: Long = 60
        private const val WRITE_TIMEOUT: Long = 60
        private const val READ_TIMEOUT: Long = 60
        private const val MAX_STALE: Int = 7
        private const val MAX_AGE: Int = 0
        private const val CACHE_SIZE: Long = 10 * 1000 * 1000 //10 MB CACHE
        private const val CACHE_CONTROL = "Cache-Control"
    }

    @Provides
    @FlickrAppScope
    fun okHttpClient(
        @FlickrNetworkInterceptor networkInterceptor: Interceptor,
        @FlickrLoggingInterceptor loggingInterceptor: HttpLoggingInterceptor,
        @StaleIfErrorInterceptor staleIfErrorInterceptor: Interceptor,
        @CacheInterceptor cacheInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(staleIfErrorInterceptor)
            .addNetworkInterceptor(cacheInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @FlickrAppScope
    @FlickrLoggingInterceptor
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.i(message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @FlickrAppScope
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, CACHE_SIZE)
    }

    @Provides
    @FlickrAppScope
    fun file(context: Context): File {
        return File(context.cacheDir, "okhttp-cache")
    }

    @Provides
    @FlickrAppScope
    @FlickrNetworkInterceptor
    fun networkInterceptor(networkUtil: NetworkUtil, cacheControl: CacheControl): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!networkUtil.isOnline()) {
                request = request.newBuilder().cacheControl(cacheControl).build()
                val response = chain.proceed(request)
                if (response.cacheResponse == null)
                    throw NoConnectivityException()
            }
            return@Interceptor chain.proceed(request)
        }
    }

    @Provides
    @FlickrAppScope
    @StaleIfErrorInterceptor
    fun staleIfErrorInterceptor(cacheControl: CacheControl): Interceptor {
        return Interceptor { chain ->
            var response: Response? = null
            val request = chain.request()
            try {
                response?.close()
                response = chain.proceed(request)
                if (response.isSuccessful) response
            } catch (e: Exception) {

            }

            if (response == null || !response.isSuccessful) {
                val newRequest = request.newBuilder().cacheControl(cacheControl).build();
                try {
                    response?.close()
                    response = chain.proceed(newRequest)
                } catch (e: Exception) {
                    throw e
                }
            }
            response
        }
    }

    @Provides
    @FlickrAppScope
    fun cacheControl(): CacheControl {
        return CacheControl.Builder()
            .maxStale(MAX_STALE, TimeUnit.DAYS)
            .maxAge(MAX_AGE, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @FlickrAppScope
    @CacheInterceptor
    fun cacheInterceptor(cacheControl: CacheControl): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
            return@Interceptor chain.proceed(request)
        }

    }
}