package com.rutershok.daily.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.R
import com.rutershok.daily.adapters.editor.FontEditorAdapter
import com.rutershok.daily.adapters.editor.TextEditorAdapter
import com.rutershok.daily.utils.FontUtil

class EditTextFragment : Fragment() {
    private var mActivity: Activity? = null
    private var mTextView: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        return layoutInflater.inflate(R.layout.fragment_edit_text, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            mActivity = activity
            mTextView = activity!!.findViewById(R.id.text_quote)
        }
        setRecyclerEditorItems(view)
        setRecyclerFonts(view)
    }

    private fun setRecyclerEditorItems(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_text_editors)
        recyclerView.adapter = TextEditorAdapter(mActivity!!)
    }

    private fun setRecyclerFonts(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_fonts)
        recyclerView.adapter = FontEditorAdapter(FontUtil.getFontsName(view.context), mTextView!!)
    }
}