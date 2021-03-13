package com.rutershok.daily

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.kc.unsplash.Unsplash
import com.kc.unsplash.Unsplash.OnPhotosLoadedListener
import com.kc.unsplash.api.Order
import com.kc.unsplash.models.Photo
import com.rutershok.daily.adapters.PhotosAdapter
import com.rutershok.daily.utils.Constant
import com.rutershok.daily.utils.Snackbar.showError
import java.util.*

class PhotosActivity : AppCompatActivity() {
    private val mPhotos: MutableList<Photo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        title = getString(R.string.choose_photo)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        initSwipeRefresh()
        initRecyclerView()
        loadPhotos()
    }

    private var mSwipeRefresh: SwipeRefreshLayout? = null
    private fun initSwipeRefresh() {
        // SwipeRefreshLayout
        mSwipeRefresh = findViewById(R.id.swipe_photos)
        (mSwipeRefresh as SwipeRefreshLayout).setOnRefreshListener(OnRefreshListener {
            mPhotos.clear()
            mPhotosAdapter.notifyDataSetChanged()
        })
        (mSwipeRefresh as SwipeRefreshLayout).setColorSchemeResources(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
    }

    private val mPhotosAdapter = PhotosAdapter(this, mPhotos)
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_photos)
        recyclerView.adapter = mPhotosAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadPhotos()
                }
            }
        })
    }

    private val mUnsplash = Unsplash(Constant.UNSPLASH_ACCESS_KEY)
    private var mPage = 1
    private fun loadPhotos() {
        mSwipeRefresh!!.isRefreshing = true
        mUnsplash.getPhotos(mPage, 18, Order.LATEST, object : OnPhotosLoadedListener {
            override fun onComplete(response: List<Photo>) {
                mSwipeRefresh!!.isRefreshing = false
                mPhotos.addAll(response)
                mPhotosAdapter.notifyDataSetChanged()
                ++mPage
            }

            override fun onError(error: String) {
                mSwipeRefresh!!.isRefreshing = false
                showError(this@PhotosActivity, R.string.error)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}