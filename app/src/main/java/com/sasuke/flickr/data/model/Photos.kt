package com.sasuke.flickr.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("page")
    @Expose
    val page: Int,

    @SerializedName("pages")
    @Expose
    val pages: Int,

    @SerializedName("perpage")
    @Expose
    val perpage: Int,

    @SerializedName("total")
    @Expose
    val total: String,

    @SerializedName("photo")
    @Expose
    val photo: List<Photo>?
)