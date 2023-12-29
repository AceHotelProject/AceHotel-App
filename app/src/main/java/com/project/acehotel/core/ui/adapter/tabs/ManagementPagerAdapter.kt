package com.project.acehotel.core.ui.adapter.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.acehotel.features.dashboard.management.finance.FinanceFragment
import com.project.acehotel.features.dashboard.management.inventory.InventoryFragment
import com.project.acehotel.features.dashboard.management.visitor.VisitorFragment

class ManagementPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FinanceFragment()
            1 -> fragment = InventoryFragment()
            2 -> fragment = VisitorFragment()
        }

        return fragment as Fragment
    }
}