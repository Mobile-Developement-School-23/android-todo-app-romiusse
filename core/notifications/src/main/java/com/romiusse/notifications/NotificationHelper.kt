package com.romiusse.notifications

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView


const val permissionPreferences = "permissionPreferences"
const val isNotifyPermGranted = "isNotifyPermGranted"

const val GRANTED = 1
const val NOT_GRANTED = 2
const val NOT_SHOWN = 0

class NotificationHelper : Fragment() {



    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        val sharedPreference =  requireContext().getSharedPreferences(permissionPreferences, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        if (isGranted) {
            editor.putInt(isNotifyPermGranted, GRANTED)
        } else {
            editor.putInt(isNotifyPermGranted, NOT_GRANTED)
        }
        editor.commit()
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_helper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialTextView>(R.id.yes_button).setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        view.findViewById<MaterialTextView>(R.id.no_button).setOnClickListener {
            val sharedPreference =  requireContext().getSharedPreferences(permissionPreferences, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putInt(isNotifyPermGranted, NOT_GRANTED)
            editor.apply()
            findNavController().navigateUp()
        }

    }

}