package com.rutershok.daily.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.rutershok.daily.R
import com.rutershok.daily.database.Storage
import com.rutershok.daily.model.DailyQuote
import com.rutershok.daily.utils.ColorUtil
import com.rutershok.daily.utils.Constant
import com.rutershok.daily.utils.Setting

class WeekQuoteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        return layoutInflater.inflate(R.layout.fragment_week_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (null != activity && null != arguments) {
            val background = if (Setting.saveData(context)) {
                ColorDrawable(ColorUtil.getBackgroundColor(context))
            } else {
                arguments!!.getString(Constant.BACKGROUND)
            }
            Glide.with(activity!!).load(background).fitCenter().centerCrop()
                .into((view.findViewById<View>(R.id.image_background_week) as ImageView))
            val textQuote = view.findViewById<TextView>(R.id.text_quote_week)
            textQuote.text = arguments!!.getString(Constant.QUOTE)
            textQuote.setTextColor(Storage.getTextColor(activity))
            view.findViewById<View>(R.id.view_line_bottom).setBackgroundColor(
                Storage.getTextColor(
                    activity
                )
            )
            val textAuthor = view.findViewById<TextView>(R.id.text_author_week)
            textAuthor.text = arguments!!.getString(Constant.AUTHOR)
            textAuthor.setTextColor(Storage.getTextColor(activity))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(dailyQuote: DailyQuote): WeekQuoteFragment {
            val bundle = Bundle()
            bundle.putString(Constant.BACKGROUND, dailyQuote.background)
            bundle.putString(Constant.QUOTE, dailyQuote.quote)
            bundle.putString(Constant.AUTHOR, dailyQuote.author)
            val fragment = WeekQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}