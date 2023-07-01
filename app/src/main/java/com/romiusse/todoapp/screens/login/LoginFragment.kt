package com.romiusse.todoapp.screens.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.romiusse.todoapp.R
import com.romiusse.todoapp.databinding.FragmentLoginBinding
import com.romiusse.todoapp.databinding.FragmentMainScreenBinding
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



    private fun login() {

        val loginOptionsBuilder: YandexAuthLoginOptions.Builder = YandexAuthLoginOptions.Builder()
        val intent: Intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        resultLauncher.launch(intent)
    }

}