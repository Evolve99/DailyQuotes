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
import com.rutershok.daily.model.DailyQuote
import com.rutershok.daily.utils.ColorUtil
import com.rutershok.daily.utils.DateUtil
import com.rutershok.daily.utils.Dialog.ShareBottomSheet
import com.rutershok.daily.utils.Setting

class DailyQuotesAdapter(
    private val activity: Activity,
    private val dailyQuotes: List<DailyQuote>
) : RecyclerView.Adapter<DailyQuotesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_quote, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyQuote = dailyQuotes[position]
        holder.imageBackground.setOnLongClickListener { v: View? ->
            ShareBottomSheet(activity, dailyQuote, holder.itemView).show()
            true
        }
        if (Setting.saveData(activity)) {
            Glide.with(activity).load(ColorDrawable(ColorUtil.getBackgroundColor(activity)))
                .into(holder.imageBackground)
        } else {
            Glide.with(activity).load(dailyQuote.background).into(holder.imageBackground)
        }
        holder.imageBackground.setOnClickListener { v: View? -> dailyQuote.openEditor(activity) }
        holder.itemView.findViewById<View>(R.id.view_line_top)
            .setBackgroundColor(Storage.getTextColor(activity))
        holder.textQuote.text = dailyQuote.quote
        holder.textQuote.setTextColor(Storage.getTextColor(activity))
        holder.itemView.findViewById<View>(R.id.view_line_bottom)
            .setBackgroundColor(Storage.getTextColor(activity))
        holder.textAuthor.text = dailyQuote.author
        holder.textAuthor.setTextColor(Storage.getTextColor(activity))
        holder.textDay.text = DateUtil.getLocalFormatDate(dailyQuote.day)
        holder.textDay.setTextColor(Storage.getTextColor(activity))
    }

    /**
     * New version of the app add this
     */
    /*private void animation(ViewHolder holder, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > mLastPosition) {
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in));
            mLastPosition = position;
        }
    }*/
    override fun getItemCount(): Int {
        return dailyQuotes.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageBackground: ImageView
        val textQuote: TextView
        val textAuthor: TextView
        val textDay: TextView

        init {
            imageBackground = view.findViewById(R.id.image_daily)
            textQuote = view.findViewById(R.id.text_quote_daily)
            textAuthor = view.findViewById(R.id.text_author_daily)
            textDay = view.findViewById(R.id.text_quote_day)
        }
    }

    init {
        setHasStableIds(true)
    }
}