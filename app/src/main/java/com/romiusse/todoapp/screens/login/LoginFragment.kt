package com.romiusse.todoapp.screens.login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.romiusse.todoapp.R
import com.romiusse.todoapp.databinding.FragmentLoginBinding
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var sdk : YandexAuthSdk

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
        sdk = YandexAuthSdk(
            requireContext(), YandexAuthOptions(requireContext())
        )
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val yandexAuthToken: YandexAuthToken? =
                            sdk.extractToken(result.resultCode, result.data)
                        if (yandexAuthToken != null) {
                            val bundle = bundleOf("token" to "OAuth ${yandexAuthToken.value}")

                            val settings: SharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(requireContext())
                            val editor = settings.edit()
                            editor.putString("server_token", "OAuth ${yandexAuthToken.value}")
                            editor.commit()

                            findNavController().navigate(
                                R.id.action_loginFragment_to_mainScreenFragment,
                                bundle
                            )
                        }
                    } catch (e: YandexAuthException) {

                    }
                }
            }
    }


    private fun offlineLogin(view: View){
        val settings = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val token = settings.getString("server_token", "")
        if(token != null && token != ""){

            val bundle = bundleOf("token" to "OAuth $token")
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

        val loginOptionsBuilder: YandexAuthLoginOptions.Builder = YandexAuthLoginOptions.Builder()
        val intent: Intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        resultLauncher.launch(intent)
    }

}