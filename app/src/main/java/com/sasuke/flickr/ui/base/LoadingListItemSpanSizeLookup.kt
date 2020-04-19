package com.sasuke.flickr.ui.base

import com.paginate.recycler.LoadingListItemSpanLookup

class LoadingListItemSpanSizeLookup(private val size: Int) : LoadingListItemSpanLookup {

    override fun getSpanSize(): Int {
        return size
    }
}