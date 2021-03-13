package com.rutershok.daily.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.R
import com.rutershok.daily.adapters.editor.BackgroundEditorAdapter
import com.rutershok.daily.adapters.editor.ImagesAdapter

class EditBackgroundFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return layoutInflater.inflate(R.layout.fragment_edit_background, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            val imageView = view.findViewById<ImageView>(R.id.image_background)

            (view.findViewById<View>(R.id.recycler_background_editors) as RecyclerView).adapter =
                BackgroundEditorAdapter(activity!!)
            (view.findViewById<View>(R.id.recycler_images) as RecyclerView).adapter =
                ImagesAdapter(view.context, imageView)
    }
}