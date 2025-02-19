package com.example.taskoo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.taskoo.R
import com.example.taskoo.databinding.FragmentRecoverAccountBinding
import com.example.taskoo.databinding.FragmentRegisterBinding
import com.example.taskoo.ui.BaseFragment
import com.example.taskoo.util.FirebaseHelper
import com.example.taskoo.util.initToolbar
import com.example.taskoo.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RecoverAccountFragment : BaseFragment() {
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListeners()
    }

    private fun initListeners() {
        binding.btnRecover.setOnClickListener {
            validateData()
            //findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun  validateData() {
        val email = binding.edtEmail.text.toString().trim()

        if(email.isNotEmpty()) {
            hideKeyboard()

            binding.progressBar.isVisible = true

            recoverAccountUser(email)

        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private fun recoverAccountUser(email: String) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                binding.progressBar.isVisible = false

                if(task.isSuccessful) {
                    showBottomSheet(
                        message = getString(R.string.text_message_recover_account_fragment)
                    )

                } else {
                    showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}