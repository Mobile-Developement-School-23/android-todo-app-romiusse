package com.romiusse.login.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.romiusse.login.R
import com.romiusse.login.databinding.FragmentLoginBinding
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken

const val TOKEN = "dithering"

/**
 *
 * Login fragment
 *
 * @author Romiusse
 */
class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var sdk : YandexAuthSdk


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            login()
        }
        binding.login2.setOnClickListener {
            offlineLogin(view)
        }
    }


    private fun offlineLogin(view: View){
        val settings = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val token = settings.getString("server_token", "")
        if(token != null && token != ""){

            val bundle = bundleOf("token" to "$token")
            findNavController().navigate(
                R.id.action_loginFragment_to_mainScreenFragment,
                bundle
            )
        }
        else{
            Snackbar.make(view, "Сначала необходимо войти по сети", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun login() {

        val bundle = bundleOf("token" to "Bearer $TOKEN")

        val settings: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = settings.edit()
        editor.putString("server_token", "Bearer $TOKEN")
        editor.commit()

        findNavController().navigate(
            R.id.action_loginFragment_to_mainScreenFragment,
            bundle
        )
    }

}
