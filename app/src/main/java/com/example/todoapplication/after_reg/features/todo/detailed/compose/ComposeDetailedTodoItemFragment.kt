package com.example.todoapplication.after_reg.features.todo.detailed.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapplication.after_reg.features.todo.detailed.DetailedTodoItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeDetailedTodoItemFragment : Fragment() {

    private val detailedTodoItemViewModel: DetailedTodoItemViewModel by viewModels()

    private val args: ComposeDetailedTodoItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val todoItemId = args.id.toString()

        lifecycleScope.launchWhenStarted {
            detailedTodoItemViewModel.setUpInfo(todoItemId)
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