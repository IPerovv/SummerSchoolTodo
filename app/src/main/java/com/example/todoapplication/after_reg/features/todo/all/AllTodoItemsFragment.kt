package com.example.todoapplication.after_reg.features.todo.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.after_reg.features.connectivity.ConnectivityObserver
import com.example.todoapplication.databinding.FragmentAllTodoItemsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AllTodoItemsFragment : Fragment() {
    private var _binding: FragmentAllTodoItemsBinding? = null
    private val binding: FragmentAllTodoItemsBinding
        get() = _binding!!

    private val allTodoItemsViewModel: AllTodoItemsViewModel by viewModels()

    private var connectedFirst = true

    private val adapter by lazy {
        JobAdapter(
            onClick = { id ->
                findNavController().navigate(
                    AllTodoItemsFragmentDirections.actionAllTodoItemsFragmentToComposeDetailedTodoItemFragment(
                        id
                    )
                )
            },
            onCheckedChange = { todoItem ->
                allTodoItemsViewModel.updateTodoItem(todoItem)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTodoItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        with(binding) {
            madeTodosCounterTv.text = adapter.getCompletedCount().toString()

            allTodoItemsRv.adapter = adapter

            allTodoItemsSr.setOnRefreshListener {
                allTodoItemsViewModel.loadAllTodoItems()
                allTodoItemsSr.isRefreshing = false
            }

            allTodoItemsFab.setOnClickListener {
                controller.navigate(
                    AllTodoItemsFragmentDirections.actionAllTodoItemsFragmentToComposeDetailedTodoItemFragment(
                        null
                    )
                )
            }

        }
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        allTodoItemsViewModel.state.onEach { state ->
            adapter.submitList(state.todoItemItems)
            binding.madeTodosCounterTv.text = adapter.getCompletedCount().toString()
        }.launchInViewLifecycleScope()

        allTodoItemsViewModel.eventFlow.onEach { event ->
            when (event) {
                is AllTodoItemsViewModel.UIEvent.ShowSnackbar -> {
                    Snackbar.make(
                        binding.root,
                        event.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }.launchInViewLifecycleScope()

        allTodoItemsViewModel.connectionFlow.onEach { status ->
            when (status) {
                ConnectivityObserver.ConnectionStatus.Available -> {
                    if (connectedFirst) {
                        connectedFirst = false
                    } else {
                        allTodoItemsViewModel.syncDataAfterConnectionLoss()
                        Snackbar.make(
                            binding.root,
                            "Соединение восстановлено, обновление данных",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                else ->
                    Snackbar.make(
                        binding.root,
                        "Connection status: $status",
                        Snackbar.LENGTH_LONG
                    ).show()
            }

        }.launchInViewLifecycleScope()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        allTodoItemsViewModel.loadAllTodoItems()
    }
}

context(Fragment)
fun <T> Flow<T>.launchInViewLifecycleScope(): Job {
    return launchIn(viewLifecycleOwner.lifecycleScope)
}
