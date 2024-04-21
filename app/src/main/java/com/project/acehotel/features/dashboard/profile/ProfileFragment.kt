package com.project.acehotel.features.dashboard.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.project.acehotel.core.utils.IUserLayout
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.databinding.FragmentProfileBinding
import com.project.acehotel.features.dashboard.profile.choose_hotel.ChooseHotelActivity
import com.project.acehotel.features.dashboard.profile.manage_franchise.ManageFranchiseActivity
import com.project.acehotel.features.dashboard.profile.manage_user.ManageUserActivity
import com.project.acehotel.features.dashboard.profile.stats.OverallStatsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), IUserLayout {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

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

        initUserInfo()

        checkUserRole()
    }

    private fun checkUserRole() {
        profileViewModel.getUser().observe(requireActivity()) { user ->
            user.user?.role?.let { changeLayoutByUser(it) }
        }
    }

    private fun initUserInfo() {
        binding.apply {
            profileViewModel.getUser().observe(this@ProfileFragment) { user ->
                tvUserEmail.text = user.user?.email
                tvUsername.text = user.user?.username
                chipUserRole.setStatus(user.user?.role?.role ?: "role")
            }
        }
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
            btnProfileManageUser.setOnClickListener {
                val intentToManageUser =
                    Intent(requireContext(), ManageUserActivity::class.java)
                startActivity(intentToManageUser)
            }
            btnProfileCustomerService.setOnClickListener {
//               wa link provided by: https://create.wa.link/
                val intentToOpenBrowser = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        CUSTOMER_SERVICE_PHONE
                    )
                )
                activity?.startActivity(intentToOpenBrowser)
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

    override fun changeLayoutByUser(userRole: UserRole) {
        when (userRole) {
            UserRole.MASTER -> {

            }
            UserRole.FRANCHISE -> {
                binding.apply {
                    btnProfileChooseHotel.visibility = View.GONE
                    btnProfileManageHotel.visibility = View.GONE
                }
            }
            UserRole.INVENTORY_STAFF -> {
                binding.apply {
                    btnProfileChooseHotel.visibility = View.GONE
                    btnProfileManageHotel.visibility = View.GONE
                    btnProfileManageUser.visibility = View.GONE
                }
            }
            UserRole.RECEPTIONIST -> {
                binding.apply {
                    btnProfileChooseHotel.visibility = View.GONE
                    btnProfileManageHotel.visibility = View.GONE
                    btnProfileManageUser.visibility = View.GONE
                }
            }
            UserRole.ADMIN -> {

            }
            UserRole.UNDEFINED -> {
                binding.apply {
                    btnProfileChooseHotel.visibility = View.GONE
                    btnProfileManageHotel.visibility = View.GONE
                    btnProfileManageUser.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private const val CUSTOMER_SERVICE_PHONE = "https://wa.link/anszxf"
    }
}