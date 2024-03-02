package com.project.acehotel.features.dashboard.room

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.project.acehotel.core.utils.formatNumber
import com.project.acehotel.databinding.FragmentRoomBinding
import com.project.acehotel.features.dashboard.room.change_price.ChangePriceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment : Fragment() {
    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleButtonChangePrice()

        fetchHotelInfo()
    }

    private fun fetchHotelInfo() {
        roomViewModel.getSelectedHotelData().observe(this) { hotel ->
            binding.apply {
                tvRoomPriceExclusive.text = "Rp ${formatNumber(hotel.exclusiveRoomPrice)}"
                tvRoomPriceRegular.text = "Rp ${formatNumber(hotel.regularRoomPrice)}"

                tvRoomDiscount.text = hotel.discount
                tvRoomDiscountPrice.text = "Rp ${formatNumber(hotel.discountAmount)}"

                tvRoomBedPrice.text = "Rp ${formatNumber(hotel.extraBedPrice)}"
            }
        }
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