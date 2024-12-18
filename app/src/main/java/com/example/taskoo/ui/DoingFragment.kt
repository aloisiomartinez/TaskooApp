package com.example.taskoo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskoo.R
import com.example.taskoo.data.model.Status
import com.example.taskoo.data.model.Task
import com.example.taskoo.databinding.FragmentDoingBinding
import com.example.taskoo.databinding.FragmentDoneBinding
import com.example.taskoo.ui.adapter.TaskAdapter


class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(getTasks())
    }

    private fun initRecyclerView(taskList: List<Task>) {
        taskAdapter = TaskAdapter(taskList)

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.setHasFixedSize(true)
        binding.rvTasks.adapter = taskAdapter
    }

    private fun getTasks() = listOf<Task>(
        Task("0", "Criar nova tela do App", Status.DOING),
        Task("1", "Criar nova tela do App de Login", Status.DOING),
        Task("2", "Criar nova tela do App de Rec SenhaDOING", Status.DOING),
        Task("3", "Salvar task", Status.DOING),
        Task("4", "Deletar Task", Status.DOING),
        Task("5", "Criar conta", Status.DOING),
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}