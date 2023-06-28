package com.romiusse.todoapp.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romiusse.todoapp.App
import com.romiusse.todoapp.screens.add.AddScreenViewModel
import com.romiusse.todoapp.screens.main.MainScreenViewModel
import java.lang.IllegalStateException

class ViewModelFactory(
    private val app: App
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            MainScreenViewModel::class.java ->{
                MainScreenViewModel(app.todoItemsRepository, app.serverTransmitter)
            }
            AddScreenViewModel::class.java ->{
                AddScreenViewModel(app.todoItemsRepository)
            }
            else -> throw IllegalStateException("Unknown View Model")

        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)