package com.rutershok.daily.adapters.editor

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rutershok.daily.R
import com.rutershok.daily.fragments.EditBackgroundFragment
import com.rutershok.daily.fragments.EditTextFragment

class EditorPagerAdapter(fragmentManager: FragmentManager?, private val mContext: Context) :
    FragmentPagerAdapter(
        fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return EditBackgroundFragment()
            1 -> return EditTextFragment()
        }
        return EditBackgroundFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return when (position) {
            0 -> mContext.getString(R.string.background)
            1 -> mContext.getString(R.string.text)
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }
}