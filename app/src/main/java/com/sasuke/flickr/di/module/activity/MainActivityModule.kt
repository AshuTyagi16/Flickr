package com.sasuke.flickr.di.module.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sasuke.flickr.di.mapkey.ViewModelKey
import com.sasuke.flickr.di.scope.PerActivityScope
import com.sasuke.flickr.ui.main.ImagesAdapter
import com.sasuke.flickr.ui.main.MainActivityViewModel
import com.sasuke.flickr.util.Constants
import com.sasuke.flickr.util.ItemDecorator
import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

    companion object {

        @Provides
        @PerActivityScope
        fun newsAdapter(picasso: Picasso): ImagesAdapter {
            return ImagesAdapter(picasso)
        }

        @Provides
        @PerActivityScope
        fun gridLayoutManager(context: Context): StaggeredGridLayoutManager {
            return StaggeredGridLayoutManager(Constants.SPAN_COUNT, RecyclerView.VERTICAL)
        }

        @Provides
        @PerActivityScope
        fun itemDecorator(): ItemDecorator {
            return ItemDecorator(Constants.SPACE)
        }

    }

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

}