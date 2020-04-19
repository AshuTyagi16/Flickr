package com.sasuke.flickr.data.network

import com.sasuke.flickr.data.model.Error
import com.sasuke.flickr.data.model.Result
import com.sasuke.flickr.util.ApiCallback
import com.sasuke.flickr.util.Constants

class FlickrRepository(private val flickrService: FlickrService) {

    fun getImageForTag(tag: String, page: Int, onGetImagesListener: OnGetImagesListener) {
        flickrService.getImageForTag(
            Constants.METHOD,
            Constants.FORMAT,
            Constants.API_KEY,
            Constants.NO_JSON_CALLBACK,
            tag,
            page
        )
            .enqueue(object : ApiCallback<Result>() {
                override fun success(response: Result) {
                    onGetImagesListener.onGetImagesSuccess(response)
                }

                override fun failure(error: Error) {
                    onGetImagesListener.onGetImagesFailure(error)
                }
            })
    }

    interface OnGetImagesListener {
        fun onGetImagesSuccess(result: Result)
        fun onGetImagesFailure(error: Error)
    }
}
