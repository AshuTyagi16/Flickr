package com.sasuke.flickr.ui.main

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sasuke.flickr.R
import com.sasuke.flickr.data.model.Photo
import com.sasuke.flickr.util.FlickrUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_image.view.*

class ImageViewHolder(itemView: View, private val picasso: Picasso) :
    RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setImage(photo: Photo) {
        picasso.load(FlickrUtils.getImageUrl(photo))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.placeholder_no_internet_connection)
            .into(itemView.ivImage)

        itemView.setOnClickListener {
            if (::onItemClickListener.isInitialized) {
                onItemClickListener.onItemClick(adapterPosition, itemView.ivImage)
            }
        }
    }

    fun setOnItemClickListenet(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, imageView: ImageView)
    }
}