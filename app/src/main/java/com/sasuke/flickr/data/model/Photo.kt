package com.sasuke.flickr.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo(

    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("owner")
    @Expose
    val owner: String,

    @SerializedName("secret")
    @Expose
    val secret: String,

    @SerializedName("server")
    @Expose
    val server: String,

    @SerializedName("farm")
    @Expose
    val farm: Int,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("ispublic")
    @Expose
    val ispublic: Int,

    @SerializedName("isfriend")
    @Expose
    val isfriend: Int,

    @SerializedName("isfamily")
    @Expose
    val isfamily: Int
)