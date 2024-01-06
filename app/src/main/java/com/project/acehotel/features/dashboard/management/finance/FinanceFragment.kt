package com.project.acehotel.features.dashboard.management.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.acehotel.databinding.FragmentFinanceBinding

class FinanceFragment : Fragment() {
    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }
}