package com.romiusse.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.romiusse.settings.databinding.FragmentSettingsScreenBinding

class SettingsScreen : Fragment() {

    lateinit var binding: FragmentSettingsScreenBinding

    companion object {
        fun newInstance() = SettingsScreen()
    }

    private lateinit var viewModel: SettingsScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsScreenViewModel::class.java]

        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

}