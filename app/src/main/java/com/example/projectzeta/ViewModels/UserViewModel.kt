package com.example.projectzeta.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectzeta.Model.User
import com.example.projectzeta.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow

class UserViewModel: ViewModel()    {

    val userId = MutableStateFlow<String>("")
    val savable = MutableStateFlow(false)
    val nameOfUser = MutableStateFlow<String>("")
    val userPhoneNumber = MutableStateFlow("")
    val userEmail = MutableStateFlow<String>("")
    val userPassword = MutableStateFlow<String>("")


    val loginUserPhone=MutableStateFlow<String>("")

    val loginPassword =MutableStateFlow<String>("")

    val userIdError = MutableStateFlow<String>("")

    val nameError = MutableStateFlow<String>("")

    val numberError = MutableStateFlow<String>("")

    val emailError = MutableStateFlow<String>("")

    val passwordError = MutableStateFlow<String>("")

    val passwordVisible = MutableStateFlow<Boolean>(false)
    val confirmPasswordVisible = MutableStateFlow<Boolean>(false)

    val confirmPassword = MutableStateFlow("")
    val confirmPasswordError = MutableStateFlow("")


    fun getUserandSetState(value:String?){
        MainRepository.getUser(value ?: "412345") { user ->
            if(user!=null){
                Log.d("TAG", user.toString())
                userId.value = user.userId
                nameOfUser.value = user.fullName
                userEmail.value = user.email
                userPhoneNumber.value = user.mobileNo
                userPassword.value = user.password
            } else {
                userPhoneNumber.value="invalid"
                Log.d("TAG", "User is null!!!")
            }
        }
    }
    fun getUserandSetStateByuserId(value:String){
        MainRepository.getUserByUserId(value) { user ->
            if(user!=null){
                Log.d("TAG", user.toString())
                userId.value = user.userId
                nameOfUser.value = user.fullName
                userEmail.value = user.email
                userPhoneNumber.value = user.mobileNo
                userPassword.value = user.password
            } else {
                userPhoneNumber.value="invalid"
                Log.d("TAG", "User is null!!!")
            }
        }
    }

    fun createUserintable(user: User){
        MainRepository.saveUser(user)
    }
    fun updateUser(user:User){
        MainRepository.saveUser(user)
    }

    fun deleteUser(){
        MainRepository.deleteUser(userId.value)
    }

    fun validate(): Boolean {
        var ok = true

        // User ID validation
        val userIdTrim = userId.value.trim()
        val userIdRegex = Regex("^\\d+$") // Digits only regex
        userIdError.value = when {
            userIdTrim.isEmpty() -> { ok = false; "User ID is required" }
            userIdTrim.length < 3 -> { ok = false; "User ID must be at least 3 characters" }
            !userIdRegex.matches(userIdTrim) -> { ok = false; "User ID must contain only digits" }
            else -> ""
        }

        val nameTrim = nameOfUser.value.trim()
        val nameRegex = Regex("^[A-Za-z][A-Za-z\\\\s\\'\\-]{1,49}\$")
        nameError.value = when {
            nameTrim.isEmpty() -> { ok = false; "Name is required" }
            !nameRegex.matches(nameTrim) -> { ok = false; "Use 2–50 letters; spaces, apostrophes, hyphens allowed" }
            else -> ""
        }

        val digitsOnly = userPhoneNumber.value.filter { it.isDigit() }
        val phoneRegex = Regex("^[6-9]\\d{9}\$")
        numberError.value = when {
            digitsOnly.isEmpty() -> { ok = false; "Phone number is required" }
            digitsOnly.length != 10 -> { ok = false; "Must be exactly 10 digits" }
            !phoneRegex.matches(digitsOnly) -> { ok = false; "Invalid mobile format (must start with 6-9)" }
            else -> ""
        }

        val emailTrim = userEmail.value.trim()
        emailError.value = when {
            emailTrim.isEmpty() -> { ok = false; "Email is required" }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches() -> {
                ok = false; "Enter a valid email address"
            }
            else -> ""
        }

        if (userPassword.value.isEmpty()) {
            passwordError.value = "Password is required"
            ok = false
        } else {
            val rules = listOf(
                Regex(".{8,}") to "8+ characters",
                Regex("[a-z]") to "one lowercase",
                Regex("[A-Z]") to "one uppercase",
                Regex("\\d") to "one digit",
                Regex("[^A-Za-z0-9]") to "one special character"
            )
            val failed = rules.filter { (rx, _) -> !rx.containsMatchIn(userPassword.value) }.map { it.second }
            passwordError.value = if (failed.isNotEmpty()) {
                ok = false; "Password must include: ${failed.joinToString(", ")}"
            } else ""
        }

        confirmPasswordError.value = when {
            confirmPassword.value.isEmpty() -> { ok = false; "Please confirm your password" }
            confirmPassword.value != userPassword.value -> { ok = false; "Passwords do not match" }
            else -> ""
        }

        return ok
    }

    fun validateLogin(phone: String, pass: String): Boolean {
        var ok = true

        val digitsOnly = phone.filter { it.isDigit() }
        val phoneRegex = Regex("^[6-9]\\d{9}$")
        numberError.value = when {
            digitsOnly.isEmpty() -> { ok = false; "Phone number is required" }
            digitsOnly.length != 10 -> { ok = false; "Must be exactly 10 digits" }
            !phoneRegex.matches(digitsOnly) -> { ok = false; "Invalid mobile format" }
            else -> ""
        }

        passwordError.value = if (pass.isEmpty()) {
            ok = false
            "Password is required"
        } else {
            ""
        }

        return ok
    }
}
