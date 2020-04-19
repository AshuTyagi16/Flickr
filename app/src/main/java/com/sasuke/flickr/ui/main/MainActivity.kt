package com.sasuke.flickr.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.paginate.Paginate
import com.sasuke.flickr.R
import com.sasuke.flickr.data.model.Photo
import com.sasuke.flickr.data.model.Status
import com.sasuke.flickr.ui.base.CustomLoadingItem
import com.sasuke.flickr.ui.base.LoadingListItemSpanSizeLookup
import com.sasuke.flickr.util.Constants
import com.sasuke.flickr.util.DebouncingQueryTextListener
import com.sasuke.flickr.util.FlickrUtils
import com.sasuke.flickr.util.ItemDecorator
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.android.support.DaggerAppCompatActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), ImagesAdapter.OnItemClickListener {

    private var images = ArrayList<Photo>()

    @Inject
    lateinit var gridLayoutManager: StaggeredGridLayoutManager

    @Inject
    lateinit var itemDecorator: ItemDecorator

    @Inject
    lateinit var adapter: ImagesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var picasso: Picasso

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var callbacks: Paginate.Callbacks
    private lateinit var paginate: Paginate

    private lateinit var tag: String

    private var page = 1

    private var isCurrentlyLoading = false

    private var hasLoadedAllTheItems = false

    private lateinit var stfalconImageViewer: StfalconImageViewer<Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject()

        setToolbar()

        setupRecyclerView()

        setupPlaceholders()

        setupListeners()

        observeLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        menu.findItem(R.id.action_search)?.let {
            searchView.setMenuItem(it)
        }
        return true
    }

    private fun inject() {
        mainActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        rvImages.layoutManager = gridLayoutManager
        rvImages.addItemDecoration(itemDecorator)
        rvImages.adapter = adapter
        adapter.setOnItemClickListenet(this)
    }

    private fun setupPagination() {
        callbacks = object : Paginate.Callbacks {
            override fun onLoadMore() {
                page++
                getImages()
            }

            override fun isLoading(): Boolean {
                return isCurrentlyLoading
            }

            override fun hasLoadedAllItems(): Boolean {
                return hasLoadedAllTheItems
            }
        }

        paginate = Paginate.with(rvImages, callbacks)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .setLoadingListItemCreator(CustomLoadingItem())
            .setLoadingListItemSpanSizeLookup(LoadingListItemSpanSizeLookup(Constants.SPAN_COUNT))
            .build()
    }

    private fun setupPlaceholders() {
        progressBar.visibility = View.GONE
        rvImages.visibility = View.GONE
        ivNoResult.visibility = View.GONE
        ivSearchSomething.visibility = View.VISIBLE
    }

    private fun setupListeners() {
        searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                this.lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isNotEmpty()) {
                        if (::tag.isInitialized && tag != it) {
                            page = 1
                            images.clear()
                            adapter.notifyDataSetChanged()
                            if (::paginate.isInitialized)
                                paginate.unbind()
                        }
                        tag = it
                        getImages()
                    }
                }
            }
        )
    }

    private fun observeLiveData() {
        mainActivityViewModel.imagesLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    it?.data?.let {
                        showSuccess()
                        it.photos?.photo?.toMutableList()?.let {
                            if (it.isNotEmpty()) {
                                val previousSize = images.size
                                images.addAll(it)
                                if (::stfalconImageViewer.isInitialized)
                                    stfalconImageViewer.updateImages(images)
                                adapter.setItems(images)
                                if (previousSize == 0) {
                                    adapter.notifyDataSetChanged()
                                    setupPagination()
                                } else
                                    adapter.notifyItemRangeInserted(
                                        previousSize,
                                        images.size - previousSize
                                    )
                            } else {
                                showNoResultFound()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    showError()
                    Toasty.error(this, "${it.message}", Toasty.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getImages() {
        if (::tag.isInitialized)
            mainActivityViewModel.getImagesForTag(tag, page)
    }

    override fun onItemClick(position: Int, imageView: ImageView) {
        zoomImage(position, imageView)
    }

    private fun zoomImage(position: Int, imageView: ImageView) {
        if (images.isNotEmpty()) {
            stfalconImageViewer =
                StfalconImageViewer.Builder<Photo>(this, images, ::loadPosterImage)
                    .withStartPosition(position)
                    .withBackgroundColorResource(android.R.color.black)
                    .withHiddenStatusBar(false)
                    .allowZooming(true)
                    .allowSwipeToDismiss(true)
                    .withTransitionFrom(imageView)
                    .build()
            stfalconImageViewer.show(true)
        }
    }

    private fun loadPosterImage(imageView: ImageView, photo: Photo) {
        picasso.load(FlickrUtils.getImageUrl(photo))
            .error(R.drawable.placeholder_no_internet_connection)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    private fun showNoResultFound() {
        rvImages.visibility = View.GONE
        progressBar.visibility = View.GONE
        ivSearchSomething.visibility = View.GONE
        ivNoResult.visibility = View.VISIBLE
    }

    private fun showLoading() {
        isCurrentlyLoading = true
        if (page == 1) {
            rvImages.visibility = View.GONE
            ivNoResult.visibility = View.GONE
            ivSearchSomething.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showSuccess() {
        isCurrentlyLoading = false
        progressBar.visibility = View.GONE
        ivNoResult.visibility = View.GONE
        ivSearchSomething.visibility = View.GONE
        rvImages.visibility = View.VISIBLE
    }

    private fun showError() {
        isCurrentlyLoading = false
        rvImages.visibility = View.GONE
        progressBar.visibility = View.GONE
        ivNoResult.visibility = View.GONE
        ivSearchSomething.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (searchView.isSearchOpen) {
            searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::paginate.isInitialized) {
            paginate.unbind()
        }
        super.onDestroy()
    }
}
