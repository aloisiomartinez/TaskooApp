package com.example.taskoo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskoo.R
import com.example.taskoo.data.model.Status
import com.example.taskoo.data.model.Task
import com.example.taskoo.databinding.FragmentFormTaskBinding
import com.example.taskoo.databinding.FragmentRecoverAccountBinding
import com.example.taskoo.util.FirebaseHelper
import com.example.taskoo.util.initToolbar
import com.example.taskoo.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*

class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        getArgs()
        initListeners()
    }

    private fun getArgs() {
        args.task.let { it ->
            if (it != null) {
                this.task = it
                configTask()
            }
        }
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            observeViewModel()

            validateData()
        }

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            status = when (id) {
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }

    }

    private fun configTask() {
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_update_form_task_fragment)
        binding.edtDescription.setText(task.description)

        setStatus()
    }

    private fun setStatus() {
        val id = when(task.status) {
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone
        }

        binding.rgStatus.check(id)
    }

    private fun validateData() {
        val description = binding.edtDescription.text.toString().trim()


        if (description.isNotEmpty()) {

            hideKeyboard()

            binding.progressBar.isVisible = true

            if (newTask) {
                task = Task()
            }

            task.description = description
            task.status = status

            if(newTask) {
                viewModel.insertTask(task)
            } else {
                viewModel.updateTask(task)
            }
            // Toast.makeText(requireContext(), "Tudo certo", Toast.LENGTH_SHORT).show()

        } else {
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun observeViewModel() {
        viewModel.taskInsert.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.text_save_success_form_task_fragment, Toast.LENGTH_SHORT).show()

            findNavController().popBackStack()
            
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.text_update_success_form_task_fragment, Toast.LENGTH_SHORT).show()

            binding.progressBar.isVisible = false

            findNavController().popBackStack()

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}