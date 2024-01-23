package com.project.acehotel.features.dashboard.room

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.acehotel.databinding.FragmentRoomBinding
import com.project.acehotel.features.dashboard.room.change_price.ChangePriceActivity

class RoomFragment : Fragment() {
    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleButtonChangePrice()
    }

    private fun handleButtonChangePrice() {
        binding.btnEditPrice.setOnClickListener {
            val intentToChangePrice = Intent(requireContext(), ChangePriceActivity::class.java)
            startActivity(intentToChangePrice)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}