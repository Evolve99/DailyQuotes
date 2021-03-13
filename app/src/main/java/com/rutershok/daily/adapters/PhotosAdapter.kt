package com.rutershok.daily.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kc.unsplash.models.Photo
import com.rutershok.daily.R
import com.rutershok.daily.utils.Constant
import com.rutershok.daily.utils.Dialog
import com.rutershok.daily.utils.Premium

class PhotosAdapter(private val mActivity: Activity, private val mPhotos: List<Photo>) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = Uri.parse(mPhotos[position].urls.regular)
        Glide.with(holder.itemView.context).setDefaultRequestOptions(RequestOptions().centerCrop())
            .load(uri).into(holder.imagePhoto)
        holder.itemView.setOnClickListener { v: View ->
            if (Premium.isPremium(v.context)) {
                mActivity.setResult(Constant.RC_PICK_IMAGE, Intent().setData(uri))
                mActivity.finish()
            } else {
                Dialog.showPremiumIsNeeded(v.context)
            }
        }
    }

    override fun getItemCount(): Int {
        return mPhotos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagePhoto: ImageView = view.findViewById(R.id.image_photo)
    }
}