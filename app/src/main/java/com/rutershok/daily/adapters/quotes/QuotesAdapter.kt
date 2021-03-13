package com.rutershok.daily.adapters.quotes

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rutershok.daily.R
import com.rutershok.daily.database.Storage
import com.rutershok.daily.model.Quote
import com.rutershok.daily.utils.*
import com.rutershok.daily.utils.Dialog.ShareBottomSheet

class QuotesAdapter(private val activity: Activity, private val quotes: List<Quote>) :
    RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_quote, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[holder.adapterPosition]
        if (Setting.saveData(activity)) {
            Glide.with(activity).load(ColorDrawable(ColorUtil.getBackgroundColor(activity)))
                .into(holder.imageBackground)
        } else {
            Glide.with(activity).load(quote.background).into(holder.imageBackground)
        }
        holder.cardQuote.setOnClickListener { v: View? -> quote.openEditor(activity) }
        holder.itemView.findViewById<View>(R.id.view_line_top)
            .setBackgroundColor(Storage.getTextColor(activity))
        holder.textQuote.text = quote.quote
        holder.textQuote.setTextColor(Storage.getTextColor(activity))
        holder.itemView.findViewById<View>(R.id.view_line_bottom)
            .setBackgroundColor(Storage.getTextColor(activity))
        holder.textAuthor.text = quote.author
        holder.textAuthor.setTextColor(Storage.getTextColor(activity))
        initOptions(holder, quote)
    }

    private fun initOptions(holder: ViewHolder, quote: Quote) {
        //Check if quote is on favorites, set properly icon
        if (Storage.getFavoriteQuotes(activity).contains(quote)) {
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_on_white)
        }
        holder.imageSave.setOnClickListener {
            ImageUtil.saveImage(
                activity,
                ImageUtil.getScaledBitmapWatermark(activity, holder.constraintQuote)
            )
        }
        holder.imageClipboard.setOnClickListener {
            Share.copyToClipboard(
                activity,
                quote.quote
            )
        }
        holder.imageFavorite.setOnClickListener { favorite(holder, quote) }
        holder.imageEdit.setOnClickListener { quote.openEditor(activity) }
        holder.imageShare.setOnClickListener {
            ShareBottomSheet(
                activity,
                quote,
                holder.constraintQuote
            ).show()
        }
    }

    private fun favorite(holder: ViewHolder, quote: Quote) {
        if (quote.isFavorite(activity)) {
            Storage.removeFavoriteQuote(activity, quote)
            Snackbar.show(activity, R.string.removed_from_favorites)
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_off_white)
        } else {
            Storage.addFavoriteQuote(activity, quote)
            Snackbar.show(activity, R.string.added_to_favorites)
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_on_white)
        }
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardQuote: CardView = view.findViewById(R.id.card_quote)
        val constraintQuote: ConstraintLayout = view.findViewById(R.id.relative_quote)
        val textQuote: TextView = view.findViewById(R.id.text_quote)
        val textAuthor: TextView = view.findViewById(R.id.text_author)
        val imageBackground: ImageView = view.findViewById(R.id.image_quote)
        val imageSave: ImageView = view.findViewById(R.id.image_save)
        val imageClipboard: ImageView = view.findViewById(R.id.image_clipboard)
        val imageFavorite: ImageView = view.findViewById(R.id.image_favorite)
        val imageEdit: ImageView = view.findViewById(R.id.image_edit)
        val imageShare: ImageView = view.findViewById(R.id.image_share)
    }

    init {
        setHasStableIds(true)
    }
}