package com.example.taskoo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.taskoo.R
import com.example.taskoo.databinding.FragmentRecoverAccountBinding
import com.example.taskoo.databinding.FragmentRegisterBinding
import com.example.taskoo.util.initToolbar
import com.example.taskoo.util.showBottomSheet


class RecoverAccountFragment : Fragment() {
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
            Toast.makeText(requireContext(), "Tudo certo", Toast.LENGTH_SHORT).show()

        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}