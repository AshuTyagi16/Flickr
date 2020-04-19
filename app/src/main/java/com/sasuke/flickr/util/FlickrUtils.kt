package com.sasuke.flickr.util

import com.sasuke.flickr.data.model.Photo

object FlickrUtils {

    fun getImageUrl(photo: Photo): String {
        return "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + ".jpg"
    }
}