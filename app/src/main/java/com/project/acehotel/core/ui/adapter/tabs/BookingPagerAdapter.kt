package com.project.acehotel.core.ui.adapter.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.acehotel.features.dashboard.booking.finished.BookingFinishedFragment
import com.project.acehotel.features.dashboard.booking.next.BookingNextFragment
import com.project.acehotel.features.dashboard.booking.now.BookingNowFragment

class BookingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = BookingNowFragment()
            1 -> fragment = BookingNextFragment()
            2 -> fragment = BookingFinishedFragment()
        }

        return fragment as Fragment
    }
}