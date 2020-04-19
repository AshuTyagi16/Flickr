package com.sasuke.flickr.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paginate.recycler.LoadingListItemCreator
import com.sasuke.flickr.R

class CustomLoadingItem : LoadingListItemCreator {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder?, p1: Int) {

    }
}