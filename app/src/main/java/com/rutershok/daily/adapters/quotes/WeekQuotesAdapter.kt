package com.rutershok.daily.adapters.quotes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rutershok.daily.fragments.WeekQuoteFragment.Companion.newInstance
import com.rutershok.daily.model.DailyQuote
import com.rutershok.daily.utils.DateUtil

class WeekQuotesAdapter(fragmentManager: FragmentManager, private val quotes: List<DailyQuote>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return newInstance(quotes[position])
    }

    override fun getCount(): Int {
        return quotes.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return DateUtil.getWeekDayName(quotes[position].day)
    }
}