package com.example.taskoo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskoo.R
import com.example.taskoo.data.model.Status
import com.example.taskoo.data.model.Task
import com.example.taskoo.databinding.FragmentHomeBinding
import com.example.taskoo.databinding.FragmentTodoBinding
import com.example.taskoo.ui.adapter.TaskAdapter
import com.example.taskoo.util.FirebaseHelper
import com.example.taskoo.util.StateView
import com.example.taskoo.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by activityViewModels()

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

        observeViewModel()

        viewModel.getTasks()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    val taskList = stateView.data?.filter { it.status == Status.TODO }

                    binding.progressBar.isVisible = false

                    listEmpty(taskList ?: emptyList())

                    taskAdapter.submitList(taskList)
                }

                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.taskInsert.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    if (stateView.data?.status == Status.TODO) {
                        //Armazena lista atual do adapter
                        val oldList = taskAdapter.currentList

                        // Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                        val newList = oldList.toMutableList().apply {
                            add(0, stateView.data)
                        }

                        // Envia a lista atualizada para o Adapter
                        taskAdapter.submitList(newList)

                        setPositionRecyclerView()
                    }

                }

                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }


        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    //Armazena lista atual do adapter
                    val oldList = taskAdapter.currentList

                    // Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                    val newList = oldList.toMutableList().apply {
                        if (!oldList.contains(stateView.data) && stateView.data?.status == Status.TODO) {
                            add(0,stateView.data)
                            setPositionRecyclerView()
                        }

                        if (stateView.data?.status == Status.TODO) {
                            find { it.id == stateView.data.id }?.description = stateView.data.description
                        } else {
                            remove(stateView.data)
                        }
                    }

                    // Armazena a posicao da tarefa a ser atualizada na lista
                    val position = newList.indexOfFirst { it.id == stateView.data?.id }

                    // Envia a lista atualizada para o Adapter
                    taskAdapter.submitList(newList)

                    // Atualiza a tarefa pela posicao do Adapter
                    taskAdapter.notifyItemChanged(position)
                }

                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }


        }

        viewModel.taskDelete.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    Toast.makeText(
                        requireContext(),
                        R.string.text_delete_success_task,
                        Toast.LENGTH_SHORT
                    ).show()

                    val oldList = taskAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        remove(stateView.data)
                    }
                    taskAdapter.submitList(newList)
                }

                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }



        }
    }

    private fun initRecyclerView() {
        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                showBottomSheet(
                    titleDialog = R.string.text_title_dialog_delete,
                    message = getString(R.string.text_message_dialog_delete),
                    titleButton = R.string.text_button_dialog_confirm,
                    onClick = {
                        viewModel.deleteTask(task)
                    }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }

            TaskAdapter.SELECT_NEXT -> {
                task.status = Status.DOING
                viewModel.updateTask(task)
            }
        }
    }

    private fun setPositionRecyclerView() {
        taskAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvTasks.scrollToPosition(0)
            }

        })
    }



    private fun listEmpty(taskList: List<Task>) {
        binding.txtInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}