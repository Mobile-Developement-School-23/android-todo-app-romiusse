package com.romiusse.edit_todo.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.romiusse.edit_todo.R
import com.romiusse.edit_todo.databinding.FragmentAddScreenBinding
import com.romiusse.edit_todo.di.EditComponentViewModel
import com.romiusse.todo_repository.TodoItem
import com.romiusse.utils.Utils
import dagger.Lazy
import java.util.Date
import javax.inject.Inject

/**
 *
 * Fragment of add screen
 *
 * @author Romiusse
 */
class AddScreenFragment : Fragment() {

    private lateinit var binding: FragmentAddScreenBinding

    @Inject
    internal lateinit var addScreenViewModelFactory: Lazy<AddScreenViewModel.Factory>

    private val viewModel: AddScreenViewModel by viewModels {
        addScreenViewModelFactory.get()
    }

    private var deadline: Date? = null
        set(value) {
            field = value
            viewModel.updateDeadline(value)
        }
    private var materialDatePicker = MaterialDatePicker.Builder.datePicker().build()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<EditComponentViewModel>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val arguments = arguments
        if(arguments != null)
            arguments.getString("id")?.let { viewModel.loadItem(id = it) }
        else
            viewModel.loadItem(new = true)

        binding = FragmentAddScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!viewModel.isNew) viewModel.item.value?.let { updateScreen(it) }


        initCalendar()
        saveButtonOnClickListener()
        closeButtonOnClickListener()
        deleteButtonOnClickListener()
        switchOnclickListener()
        observeItem()

    }

    private fun saveButtonOnClickListener(){
        binding.saveButton.setOnClickListener {
            if(binding.edittextAdding.text.toString() != "") {
                if(arguments != null) updateItem()
                else createItem()
                close()
            }
            else Toast.makeText(context, getString(R.string.null_text), Toast.LENGTH_SHORT).show()
        }
    }
    private fun closeButtonOnClickListener(){
        binding.closeButton.setOnClickListener {
            close()
        }
    }
    private fun deleteButtonOnClickListener(){
        binding.deleteButton.setOnClickListener {
            deleteItem()
            close()
        }
    }

    private fun switchOnclickListener(){
        binding.switchdeadline.setOnClickListener {
            if(binding.switchdeadline.isChecked){
                materialDatePicker.show(parentFragmentManager, "tag")
                if(deadline == null) deadline = Date()
                binding.textviewCalendardate.visibility = View.VISIBLE
                binding.textviewCalendardate.text = Utils.convertDateToString(deadline)
            }
            else{
                binding.textviewCalendardate.visibility = View.GONE
                deadline = null
            }
        }
    }
    private fun observeItem(){
        viewModel.item.observe(viewLifecycleOwner, Observer {

            if(viewModel.isNew) binding.edittextAdding.setText(it.text)
            else viewModel.updateText(binding.edittextAdding.text.toString())
            updateScreen(it)
            viewModel.isNew = false
        })
    }

    private fun initCalendar() {
        binding.textviewCalendardate.setOnClickListener {
            materialDatePicker.show(parentFragmentManager, "tag")
        }
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.textviewCalendardate.visibility = View.VISIBLE
            deadline = Date(it)
            binding.textviewCalendardate.text = Utils.convertDateToString(deadline)
        }

    }

    private fun updateScreen(item: TodoItem) {
        item?.let {
            binding.spinnerAdding.setSelection(Utils.getPosFromPriority(it.priority))
            it.deadline?.let{dl ->
                deadline = dl
                binding.switchdeadline.isChecked = true
                binding.textviewCalendardate.visibility = View.VISIBLE
                binding.textviewCalendardate.text = Utils.convertDateToString(deadline)
            }
            binding.edittextAdding.setText(it.text)
        }
    }

    private fun createItem(){
        val item = TodoItem(
            id = Date().time.toString(),
            text = binding.edittextAdding.text.toString(),
            priority = Utils.convertPosToPriority(binding.spinnerAdding.selectedItemPosition),
            flag = false,
            deadline = deadline,
            createdAt = Date(),
            changedAt = Date()
        )
        viewModel.createItem(item)
    }

    private fun deleteItem(){
        viewModel.deleteItem(viewModel.item.value)
    }

    private fun updateItem(){
        val item = viewModel.item.value
        val i = item?.let {
            TodoItem(
                id = it.id,
                text = binding.edittextAdding.text.toString(),
                priority = Utils.convertPosToPriority(binding.spinnerAdding.selectedItemPosition),
                flag = it.flag,
                deadline = deadline,
                createdAt = it.createdAt,
                changedAt = Date()
            )
        }
        viewModel.updateItem(i)
    }

    private fun close(){

        findNavController().navigateUp()
    }


}
