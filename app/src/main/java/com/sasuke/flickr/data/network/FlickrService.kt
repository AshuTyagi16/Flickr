package com.sasuke.flickr.data.network

import com.sasuke.flickr.data.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("rest")
    fun getImageForTag(
        @Query("method") method: String,
        @Query("format") format: String,
        @Query("api_key") api_key: String,
        @Query("nojsoncallback") nojsoncallback: Int,
        @Query("tags") tags: String,
        @Query("page") page: Int
    ): Call<Result>
}