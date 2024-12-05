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
import com.example.taskoo.util.initToolbar


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            validateData()
            //findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun  validateData() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.toString().trim()

        if(email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                Toast.makeText(requireContext(), "Tudo certo", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "Preencha uma senha", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Preencha um e-mail válido", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}