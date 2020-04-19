package com.sasuke.flickr.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("photos")
    @Expose
    val photos: Photos?,

    @SerializedName("stat")
    @Expose
    val stat: String,

    @SerializedName("code")
    @Expose
    val code: Int
)
