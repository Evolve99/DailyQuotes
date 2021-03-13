package com.rutershok.daily.adapters.quotes

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rutershok.daily.QuotesActivity
import com.rutershok.daily.R
import com.rutershok.daily.model.Category
import com.rutershok.daily.model.Category.Companion.getImageRes
import com.rutershok.daily.model.Category.Companion.getKeyRes
import com.rutershok.daily.model.Category.Companion.getTitleRes
import com.rutershok.daily.utils.Constant
import com.rutershok.daily.utils.FontUtil

class CategoriesAdapter(private val activity: Activity) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private val mTypeface: Typeface = FontUtil.getFont(activity, "free_Monthoers.otf")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryKey = activity.getString(getKeyRes(position))
        val categoryTitle = activity.getString(getTitleRes(position))
        Glide.with(activity).load(getImageRes(position)).into(holder.imageCategory)
        holder.textCategory.text = categoryTitle
        holder.textCategory.typeface = mTypeface
        holder.cardCategory.setOnClickListener { v: View? ->
            activity.startActivity(
                Intent(activity, QuotesActivity::class.java)
                    .putExtra(Constant.CATEGORY_TITLE, categoryTitle)
                    .putExtra(Constant.CATEGORY_KEY, categoryKey)
            )
        }
    }

    override fun getItemCount(): Int {
        return Category.values().size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardCategory: CardView
        val imageCategory: ImageView
        val textCategory: TextView

        init {
            cardCategory = view.findViewById(R.id.card_category)
            imageCategory = view.findViewById(R.id.image_category)
            textCategory = view.findViewById(R.id.text_category)
        }
    }

    init {
        setHasStableIds(true)
    }
}