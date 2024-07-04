package com.example.todoapplication.after_reg.features.todo.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComposeDetailedTodoItemFragment : Fragment() {

    private val detailedTodoItemViewModel: DetailedTodoItemViewModel by viewModels()

    private val args: ComposeDetailedTodoItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val todoItemId = args.id.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailedTodoItemViewModel.setUpInfo(todoItemId)
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                DetailedTodoItemScreen(
                    viewModel = detailedTodoItemViewModel,
                    onBack = { findNavController().popBackStack() },
                    onSave = { findNavController().popBackStack() }, //TODO: Переделать под сохранение
                    onDelete = { findNavController().popBackStack() } //TODO: Переделать под удаление
                )
            }
        }
    }
}