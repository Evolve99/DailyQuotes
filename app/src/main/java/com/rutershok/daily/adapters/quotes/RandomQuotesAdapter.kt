package com.rutershok.daily.adapters.quotes

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rutershok.daily.R
import com.rutershok.daily.database.Storage
import com.rutershok.daily.model.Quote
import com.rutershok.daily.utils.ColorUtil
import com.rutershok.daily.utils.Setting

class RandomQuotesAdapter(private val activity: Activity, private val quotes: List<Quote>) :
    RecyclerView.Adapter<RandomQuotesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_random, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (Setting.saveData(activity)) {
            Glide.with(activity).load(ColorDrawable(ColorUtil.getBackgroundColor(activity)))
                .into(holder.imageBackground)
        } else {
            Glide.with(activity).load(quotes[position].background).into(holder.imageBackground)
        }
        holder.textQuote.text = quotes[position].quote
        holder.textQuote.setTextColor(Storage.getTextColor(activity))
        holder.itemView.findViewById<View>(R.id.view_line_bottom)
            .setBackgroundColor(Storage.getTextColor(activity))
        holder.textAuthor.text = quotes[position].author
        holder.textAuthor.setTextColor(Storage.getTextColor(activity))
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val imageBackground: ImageView = view.findViewById(R.id.image_background_random)
        val textQuote: TextView = view.findViewById(R.id.text_quote_random)
        val textAuthor: TextView = view.findViewById(R.id.text_author_random)
    }
}