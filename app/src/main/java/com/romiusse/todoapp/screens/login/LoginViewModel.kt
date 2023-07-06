package com.romiusse.todoapp.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory
}
