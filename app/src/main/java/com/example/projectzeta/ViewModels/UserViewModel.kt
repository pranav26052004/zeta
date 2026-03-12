package com.example.projectzeta.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectzeta.constants.FirebaseDatabases
import com.example.projectzeta.Model.User
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow

class UserViewModel: ViewModel()    {

    val userId = MutableStateFlow<String>("")
    val savable = MutableStateFlow(false)

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


    fun getUserandSetState(value:String){
        RealtimeFirebaseHelper.readItemUsingProperty(FirebaseDatabases.USER_TABLE, "mobileNo", value, User::class.java){user->
            if(user!=null){
                Log.d("TAG", user.toString())
                userId.value = user.userId
                nameOfUser.value = user.fullName
                userEmail.value = user.email
                userPhoneNumber.value = user.mobileNo
                userPassword.value = user.password
            } else {
                Log.d("TAG", "User is null!!!")
            }
        }
    }

    fun updateUser(user:User){
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.USER_TABLE, user.userId, user)
    }

}