package com.sasuke.flickr.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sasuke.flickr.R
import com.sasuke.flickr.data.model.Photo
import com.squareup.picasso.Picasso

class ImagesAdapter(private val picasso: Picasso) : RecyclerView.Adapter<ImageViewHolder>(),
    ImageViewHolder.OnItemClickListener {

    private lateinit var photos: List<Photo>
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_image, parent, false)
        return ImageViewHolder(view, picasso)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (::photos.isInitialized) {
            holder.setImage(photos.get(position))
            holder.setOnItemClickListenet(this)
        }
    }

    override fun getItemCount(): Int {
        return if (::photos.isInitialized) photos.size else 0
    }

    fun setItems(photos: List<Photo>) {
        this.photos = photos
    }

    override fun onItemClick(position: Int, imageView: ImageView) {
        if (::onItemClickListener.isInitialized) {
            onItemClickListener.onItemClick(position, imageView)
        }
    }

    fun setOnItemClickListenet(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, imageView: ImageView)
    }

}