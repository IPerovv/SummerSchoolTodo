package com.example.todoapplication.after_reg.features.todo.detailed

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapplication.R
import com.example.todoapplication.databinding.FragmentDetailedTodoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class DetailedTodoItemFragment : Fragment() {
    private var _binding: FragmentDetailedTodoItemBinding? = null
    private val binding: FragmentDetailedTodoItemBinding
        get() = _binding!!

    private val detailedTodoItemViewModel: DetailedTodoItemViewModel by viewModels()

    private val args: DetailedTodoItemFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            detailedTodoItemViewModel.setUpInfo(args.id, resources)
        }

        detailedTodoItemViewModel.selectedImportance.collectInViewLifecycleScope { importance ->
            binding.subtitle.text = importance
        }

        detailedTodoItemViewModel.deadline.collectInViewLifecycleScope { date ->
            binding.dateSwitch.isChecked = date != " "
            binding.showDateTv.text = date.toString()
        }


        detailedTodoItemViewModel.todoBody.collectInViewLifecycleScope { text ->
            binding.editText.text = Editable.Factory.getInstance().newEditable(text)
        }



        binding.importanceBlock.setOnClickListener {
            showImportanceMenu()
        }

        binding.backButton.setOnClickListener {
            //clearFocusTv()
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            //clearFocusTv()
            findNavController().popBackStack() //TODO: переделать под сохранение объекта
        }

        binding.deleteBlock.setOnClickListener {
            //clearFocusTv()
            findNavController().popBackStack() //TODO:  переделеать под удаление объекта
        }

        binding.dateSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (binding.showDateTv.text == " ") {
                    showDatePicker()
                }
                binding.dateBlock.setOnClickListener {
                    showDatePicker()
                }
            } else {
                binding.dateBlock.setOnClickListener(null)
            }
        }
    }

    private fun showImportanceMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.importanceBlock)
        popupMenu.menuInflater.inflate(R.menu.importance_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            detailedTodoItemViewModel.setupPopupMenu(menuItem.itemId, resources = resources)
            true
        }
        popupMenu.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year: Int, month: Int, dayOfMonth: Int ->
                detailedTodoItemViewModel.setNewDate(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    private fun clearFocusTv(context: Context, view: View?){
//        val imm =
//            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view?.windowToken,0)
//        //TODO: Закрывать клавиатуру при нажатии на кнопку или в любое другое место кроме ET
//    }
}

context(Fragment)
fun <T> Flow<T>.collectInViewLifecycleScope(action: suspend (T) -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        collect { value ->
            action(value)
        }
    }
}