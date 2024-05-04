package com.project.acehotel.features.dashboard.management

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.project.acehotel.R
import com.project.acehotel.core.ui.adapter.tabs.ManagementPagerAdapter
import com.project.acehotel.databinding.FragmentManagementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment : Fragment() {
    private var _binding: FragmentManagementBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()

        setupSearch()
    }

    private fun setupSearch() {
        binding.edManagementSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                handleSearch(p0.toString())
            }
        })

        binding.edManagementSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearch(binding.edManagementSearch.text.toString())

                binding.edManagementSearch.clearFocus()
                val manager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view?.windowToken, 0)
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
        val adapter = ManagementPagerAdapter(this)

        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        for (i in 0..3) {
            val customTextView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            binding.tabLayout.getTabAt(i)?.customView = customTextView
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isEditTextEditable(position != 0) // Assuming you want search disabled only on the first tab
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun isEditTextEditable(isEditable: Boolean) {
        binding.edManagementSearch.apply {
            isFocusable = isEditable
            isClickable = isEditable
            isFocusableInTouchMode = isEditable
            isCursorVisible = isEditable
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_finance,
            R.string.tab_inventory,
            R.string.tab_visitor,
        )
    }
}