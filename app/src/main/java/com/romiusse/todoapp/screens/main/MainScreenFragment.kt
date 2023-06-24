package com.romiusse.todoapp.screens.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.romiusse.todoapp.R

class MainScreenFragment : Fragment() {

    companion object {
        fun newInstance() = MainScreenFragment()
    }

    private lateinit var viewModel: MainScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        // TODO: Use the ViewModel
    }

}