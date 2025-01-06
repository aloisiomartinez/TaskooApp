package com.example.taskoo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskoo.R
import com.example.taskoo.data.model.Status
import com.example.taskoo.data.model.Task
import com.example.taskoo.databinding.FragmentHomeBinding
import com.example.taskoo.databinding.FragmentTodoBinding
import com.example.taskoo.ui.adapter.TaskAdapter
import com.example.taskoo.ui.adapter.TaskTopAdapter


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskTopAdapter: TaskTopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initRecyclerView()
        getTasks()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }


    private fun initRecyclerView() {
        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)
        }

        taskTopAdapter = TaskTopAdapter() { task, option ->
            optionSelected(task, option)
        }

        val concatAdapter = ConcatAdapter(
            taskTopAdapter,
            taskAdapter
        )

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = concatAdapter
        }

    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                Toast.makeText(
                    requireContext(),
                    "Removendo ${task.description}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }

            TaskAdapter.SELECT_NEXT -> {
                Toast.makeText(requireContext(), "Next ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun getTasks() {
        val taskTopList = listOf(
            Task("0", "TSKTOPCriar nova tela do App", Status.TODO),
            Task("1", "TSKTOPCriar nova tela do App de Login", Status.TODO),
        )

        val taskList = listOf(
            Task("0", "Criar nova tela do App", Status.TODO),
            Task("1", "Criar nova tela do App de Login", Status.TODO),
            Task("2", "Criar nova tela do App de Rec Senha", Status.TODO),
            Task("3", "Salvar task", Status.TODO),
            Task("4", "Deletar Task", Status.TODO),
            Task("5", "Criar conta", Status.TODO)
        )

        taskAdapter.submitList(taskList)
        taskTopAdapter.submitList(taskTopList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}