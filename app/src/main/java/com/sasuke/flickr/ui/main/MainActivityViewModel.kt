package com.sasuke.flickr.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sasuke.flickr.data.model.Error
import com.sasuke.flickr.data.model.Resource
import com.sasuke.flickr.data.model.Result
import com.sasuke.flickr.data.network.FlickrRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val flickrRepository: FlickrRepository
) :
    ViewModel(),
    FlickrRepository.OnGetImagesListener {

    private val _imagesLiveData = MutableLiveData<Resource<Result>>()
    val imagesLiveData: LiveData<Resource<Result>>
        get() = _imagesLiveData

    fun getImagesForTag(tag: String, page: Int) {
        _imagesLiveData.postValue(Resource.loading())
        flickrRepository.getImageForTag(tag, page, this)
    }

    override fun onGetImagesSuccess(result: Result) {
        _imagesLiveData.postValue(Resource.success(result))
    }

    override fun onGetImagesFailure(error: Error) {
        _imagesLiveData.postValue(Resource.error(error))
    }

}