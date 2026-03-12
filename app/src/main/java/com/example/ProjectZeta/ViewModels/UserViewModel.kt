package com.example.ProjectZeta.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ProjectZeta.constants.FirebaseDatabases
import com.example.projectzeta.Model.User
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class UserViewModel: ViewModel()    {
    val userId = MutableStateFlow<String>("")

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
                userId.value = user.userId
                nameOfUser.value = user.fullName
                userEmail.value = user.email
                userPhoneNumber.value = user.mobileNo
            } else {
                Log.d("TAG", "User is null!!!")
            }
        }
    }

    fun updateProfile(fullName:String, mobileNo:String){

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val user = User(
            uid,
            fullName,
            mobileNo,
            email = FirebaseAuth.getInstance().currentUser!!.email!!
        )

        RealtimeFirebaseHelper.writeItem("usersTable", uid, user)

    }

    fun changePassword(newPassword:String){
        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(newPassword)

    }

    fun changeEmail(newEmail:String){
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid

        user.verifyBeforeUpdateEmail(newEmail)?.addOnSuccessListener { task->
            RealtimeFirebaseHelper.updateUserEmail(uid, newEmail)
        }
    }

    fun deleteUserAccount(key: String){
        RealtimeFirebaseHelper.deleteItem(FirebaseDatabases.USER_TABLE, key)
    }

}