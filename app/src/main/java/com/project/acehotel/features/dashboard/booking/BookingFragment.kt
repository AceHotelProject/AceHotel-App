package com.project.acehotel.features.dashboard.booking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.acehotel.R
import com.project.acehotel.core.ui.adapter.tabs.BookingPagerAdapter
import com.project.acehotel.databinding.FragmentBookingBinding
import com.project.acehotel.features.dashboard.management.IManagementSearch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()

        setupSearch()
    }

    private fun setupSearch() {
        binding.edBookingSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                handleSearch(p0.toString())
            }
        })

        binding.edBookingSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearch(binding.edBookingSearch.text.toString())
                true // consume the action
            } else {
                false // pass on to other listeners.
            }
        }
    }

    private fun handleSearch(query: String) {
        val currentFragment =
            childFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem}")
        if (currentFragment is IManagementSearch) {
            currentFragment.onSearchQuery(query)
        }
    }

    private fun setupTabs() {
        val adapter = BookingPagerAdapter(this)

        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        for (i in 0..3) {
            val customTextView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            binding.tabLayout.getTabAt(i)?.customView = customTextView
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_booking_today,
            R.string.tab_booking_next,
            R.string.tab_booking_done,
        )
    }
}