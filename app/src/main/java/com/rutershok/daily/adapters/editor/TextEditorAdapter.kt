package com.rutershok.daily.adapters.editor

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.R
import com.rutershok.daily.model.Editor
import com.rutershok.daily.utils.Dialog

class TextEditorAdapter(private val activity: Activity) :
    RecyclerView.Adapter<TextEditorAdapter.ViewHolder>() {
    private val editors = Editor.Text.values()
    private val mTextView: TextView = activity.findViewById(R.id.text_quote)
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
                Editor.Text.TEXT_COLOR -> Dialog.showChangeTextColor(activity, mTextView)
                Editor.Text.TEXT_SIZE -> Dialog.showChangeTextSize(activity, mTextView)
                Editor.Text.TEXT_ALIGNMENT -> when (mTextView.gravity) {
                    Gravity.END, 8388661 -> mTextView.gravity = Gravity.START
                    Gravity.CENTER -> mTextView.gravity = Gravity.END
                    Gravity.START, 8388659 -> mTextView.gravity = Gravity.CENTER
                }
                Editor.Text.TEXT_OPACITY -> Dialog.showChangeTextOpacity(activity, mTextView)
                Editor.Text.TEXT_SPACING -> Dialog.showChangeTextSpacing(activity, mTextView)
                Editor.Text.TEXT_HIGHLIGHT -> Dialog.showChangeTextHighlight(activity, mTextView)
                Editor.Text.TEXT_BORDER -> Dialog.showChangeTextBorder(activity, mTextView)
            }
        }
    }

    override fun getItemCount(): Int {
        return editors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintEditor: ConstraintLayout = view.findViewById(R.id.cl_editor)
        val imageIcon: ImageView = view.findViewById(R.id.image_editor_icon)
        val textTitle: TextView = view.findViewById(R.id.text_editor_title)
    }

}