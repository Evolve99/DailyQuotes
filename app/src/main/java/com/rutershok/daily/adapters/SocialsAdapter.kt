package com.rutershok.daily.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.R
import com.rutershok.daily.model.Social.Companion.list
import com.rutershok.daily.utils.Share

class SocialsAdapter(private val mActivity: Activity, private val mView: View) :
    RecyclerView.Adapter<SocialsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_social, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageSocial.setImageResource(list[position].iconResId)
        holder.cardSocial.setOnClickListener { v: View? ->
            Share.withSocial(
                mActivity,
                mView,
                list[holder.adapterPosition]
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardSocial: CardView = view.findViewById(R.id.card_social)
        val imageSocial: ImageView = view.findViewById(R.id.image_social)

    }
}