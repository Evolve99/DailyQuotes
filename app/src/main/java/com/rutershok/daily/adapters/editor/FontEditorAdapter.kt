package com.rutershok.daily.adapters.editor

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.R
import com.rutershok.daily.utils.Dialog
import com.rutershok.daily.utils.FontUtil
import com.rutershok.daily.utils.Premium

class FontEditorAdapter(private val fonts: List<String>, private val textView: TextView) :
    RecyclerView.Adapter<FontEditorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_editor_font, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fontName = fonts[holder.adapterPosition]
        val typeface = Typeface.createFromAsset(holder.itemView.context.assets, "fonts/$fontName")
        holder.textFont.typeface = typeface
        if (FontUtil.getIsPremium(fontName) && !Premium.isPremium(holder.itemView.context)) {
            holder.textPro.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener { v: View ->
            if (FontUtil.getIsPremium(fontName)) {
                if (Premium.isPremium(v.context)) {
                    textView.typeface = typeface
                } else {
                    Dialog.showPremiumIsNeeded(v.context)
                }
            } else {
                textView.typeface = typeface
            }
        }
    }

    override fun getItemCount(): Int {
        return fonts.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textFont: TextView
        val textPro: TextView

        init {
            textFont = view.findViewById(R.id.text_font)
            textPro = view.findViewById(R.id.text_pro_font)
        }
    }

    init {
        setHasStableIds(true)
    }
}