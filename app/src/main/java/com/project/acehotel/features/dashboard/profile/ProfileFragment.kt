package com.project.acehotel.features.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentProfileBinding
import com.project.acehotel.features.dashboard.profile.choose_hotel.ChooseHotelActivity
import com.project.acehotel.features.dashboard.profile.manage_franchise.ManageFranchiseActivity
import com.project.acehotel.features.dashboard.profile.stats.OverallStatsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleMenu()
    }

    private fun handleMenu() {
        binding.apply {
            btnProfileChooseHotel.setOnClickListener {
                val intentToChooseHotel = Intent(requireContext(), ChooseHotelActivity::class.java)
                startActivity(intentToChooseHotel)
            }
            btnProfileStatTotal.setOnClickListener {
                val intentToOverallStat = Intent(requireContext(), OverallStatsActivity::class.java)
                startActivity(intentToOverallStat)
            }
            btnProfileManageHotel.setOnClickListener {
                val intentToManageFranchise =
                    Intent(requireContext(), ManageFranchiseActivity::class.java)
                startActivity(intentToManageFranchise)
            }
            btnProfileCustomerService.setOnClickListener {
                activity?.showToast("Customer Service")
            }
            binding.btnProfileLogout.setOnClickListener {
                LogoutDialog().show(parentFragmentManager, "Logout Dialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}