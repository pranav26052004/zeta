package com.example.ProjectZeta.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel: ViewModel()    {

    val nameOfUser = MutableStateFlow<String>("")
    val userPhoneNumber = MutableStateFlow("")

    val userEmail = MutableStateFlow<String>("")

    val userPassword = MutableStateFlow<String>("")

    val nameError = MutableStateFlow<String>("")

    val numberError = MutableStateFlow<String>("")

    val emailError = MutableStateFlow<String>("")

    val passwordError = MutableStateFlow<String>("")

    val passwordVisible = MutableStateFlow<Boolean>(false)

    val confirmPassword = MutableStateFlow("")
    val confirmPasswordError = MutableStateFlow("")

}