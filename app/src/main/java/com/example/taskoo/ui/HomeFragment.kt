package com.example.taskoo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.taskoo.R
import com.example.taskoo.databinding.FragmentHomeBinding
import com.example.taskoo.databinding.FragmentSplashBinding
import com.example.taskoo.ui.adapter.ViewPageAdapter
import com.example.taskoo.util.showBottomSheet
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        initListeners()
        initTabs()
    }

    private fun initListeners() {
        binding.btnLogout.setOnClickListener {
            showBottomSheet(
                titleButton = R.string.text_button_dialog_confirm,
                titleDialog = R.string.text_title_dialog_confirm_logout,
                message = getString(R.string.text_message_dialog_confirm_logout),
                onClick = {
                    auth.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_authentication)
                }
            )
        }
    }

    private fun initTabs() {
        val pageAdapter = ViewPageAdapter(requireActivity())
        binding.viewPager.adapter = pageAdapter

        pageAdapter.addFragment(TodoFragment(), R.string.status_task_todo)
        pageAdapter.addFragment(DoingFragment(), R.string.status_task_doing)
        pageAdapter.addFragment(DoneFragment(), R.string.status_task_done)

        binding.viewPager.offscreenPageLimit = pageAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(pageAdapter.getTitle(position))
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}