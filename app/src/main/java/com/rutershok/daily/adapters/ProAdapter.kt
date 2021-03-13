package com.rutershok.daily.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rutershok.daily.R
import com.rutershok.daily.model.ProFeature

class ProAdapter(context: Context, resource: Int, objects: List<ProFeature?>) :
    ArrayAdapter<ProFeature?>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val proFeature = ProFeature.values()[position]
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_pro_feature, null)
        }
        val imageIcon = view!!.findViewById<ImageView>(R.id.image_pro_icon)
        imageIcon.setImageResource(proFeature.iconRes)
        val textTitle = view.findViewById<TextView>(R.id.text_pro_title)
        textTitle.setText(proFeature.title)
        return view
    }
}