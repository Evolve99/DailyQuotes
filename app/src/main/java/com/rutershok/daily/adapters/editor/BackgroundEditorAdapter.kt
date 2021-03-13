package com.rutershok.daily.adapters.editor

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.PhotosActivity
import com.rutershok.daily.R
import com.rutershok.daily.model.Editor.Background
import com.rutershok.daily.utils.Constant
import com.rutershok.daily.utils.Dialog

class BackgroundEditorAdapter(private val activity: Activity) :
    RecyclerView.Adapter<BackgroundEditorAdapter.ViewHolder>() {
    private val editors: Array<Background> = Background.values()
    private val mImageBackground: ImageView = activity.findViewById(R.id.image_background)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_editor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editor = editors[position]
        holder.imageIcon.setImageResource(editor.iconRes)
        holder.textTitle.setText(editor.titleRes)
        holder.constraintEditor.setOnClickListener { v: View? ->
            when (editor) {
                Background.BACKGROUND_COLOR -> Dialog.showChangeBackgroundColor(
                    activity,
                    mImageBackground
                )
                Background.BACKGROUND_GALLERY -> activity.startActivityForResult(
                    Intent.createChooser(
                        Intent()
                            .setType("image/*")
                            .setAction(Intent.ACTION_GET_CONTENT),
                        activity.getString(R.string.choose_image)
                    ),
                    Constant.RC_PICK_IMAGE
                )
                Background.BACKGROUND_GRADIENT -> Dialog.showChangeBackgroundGradient(
                    activity,
                    mImageBackground
                )
                Background.BACKGROUND_OPACITY -> Dialog.showChangeBackgroundOpacity(
                    activity,
                    mImageBackground
                )
                Background.BACKGROUND_PHOTOS -> activity.startActivityForResult(
                    Intent(
                        activity,
                        PhotosActivity::class.java
                    ), Constant.RC_PICK_IMAGE
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return editors.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintEditor: ConstraintLayout = view.findViewById(R.id.cl_editor)
        val imageIcon: ImageView = view.findViewById(R.id.image_editor_icon)
        val textTitle: TextView = view.findViewById(R.id.text_editor_title)
    }

}