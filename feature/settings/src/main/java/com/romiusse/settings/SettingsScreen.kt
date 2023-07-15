package com.romiusse.settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.romiusse.settings.databinding.FragmentSettingsScreenBinding

const val themeStyle = "themeStyle"
const val selectedTheme = "selectedTheme"

const val LIGHT = 1
const val DARK = 2
const val SYSTEM = 0

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

        val sharedPreference =
            requireContext().getSharedPreferences(themeStyle, Context.MODE_PRIVATE)
        val type = sharedPreference.getInt(selectedTheme, SYSTEM)

        binding.apply {
            when (type) {
                LIGHT -> light.isChecked = true
                DARK -> dark.isChecked = true
                SYSTEM -> system.isChecked = true
            }
        }

        binding.light.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked) {
                val sharedPreference =
                    requireContext().getSharedPreferences(themeStyle, Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putInt(selectedTheme, LIGHT)
                editor.apply()
                activity?.recreate()
            }
        }
        binding.dark.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val sharedPreference =
                    requireContext().getSharedPreferences(themeStyle, Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putInt(selectedTheme, DARK)
                editor.apply()
                activity?.recreate()
            }

        }
        binding.system.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val sharedPreference =
                    requireContext().getSharedPreferences(themeStyle, Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putInt(selectedTheme, SYSTEM)
                editor.apply()
                activity?.recreate()
            }

        }

    }

}