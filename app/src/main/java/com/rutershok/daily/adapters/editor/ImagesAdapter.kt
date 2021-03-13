package com.rutershok.daily.adapters.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rutershok.daily.R
import com.rutershok.daily.utils.ImageUtil

class ImagesAdapter(context: Context?, private val imageView: ImageView) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private val images = ImageUtil.getImages(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_editor_background, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(images[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().override(holder.itemView.width, holder.itemView.height))
            .into(holder.imageImage)
        holder.imageImage.setOnClickListener { imageView.setImageDrawable(images[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageImage: ImageView = view.findViewById(R.id.image_image)
    }

}