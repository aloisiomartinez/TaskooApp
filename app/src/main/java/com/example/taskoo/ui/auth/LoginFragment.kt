package com.example.taskoo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.taskoo.R
import com.example.taskoo.databinding.FragmentLoginBinding
import com.example.taskoo.databinding.FragmentRegisterBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }

        binding.btnLogin.setOnClickListener {
            validateData()
        }
    }

    private fun  validateData() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.toString().trim()

        if(email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Preencha seu e-mail", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Preencha seu e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}