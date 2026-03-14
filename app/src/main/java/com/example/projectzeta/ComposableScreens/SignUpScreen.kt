package com.example.projectzeta.ComposableScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.Model.User
import com.example.projectzeta.ViewModels.UserViewModel

@Composable
fun SignUpScreen(
    viewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    val userId by viewModel.userId.collectAsState()
    val name by viewModel.nameOfUser.collectAsState()
    val number by viewModel.userPhoneNumber.collectAsState()
    val email by viewModel.userEmail.collectAsState()
    val password by viewModel.userPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val confirmPasswordVisible by viewModel.confirmPasswordVisible.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val numberError by viewModel.numberError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { viewModel.userId.value = it },
                        label = { Text("userId") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            viewModel.nameOfUser.value = it
                            if (nameError.isNotEmpty()) viewModel.nameError.value = ""
                        },
                        label = { Text("Name") },
                        singleLine = true,
                        isError = nameError.isNotEmpty(),
                        supportingText = { if (nameError.isNotEmpty()) Text(nameError) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = number,
                        onValueChange = {
                            viewModel.userPhoneNumber.value = it.filter { c -> c.isDigit() }
                            if (numberError.isNotEmpty()) viewModel.numberError.value = ""
                        },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        isError = numberError.isNotEmpty(),
                        supportingText = { if (numberError.isNotEmpty()) Text(numberError) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            viewModel.userEmail.value = it
                            if (emailError.isNotEmpty()) viewModel.emailError.value = ""
                        },
                        label = { Text("Email") },
                        singleLine = true,
                        isError = emailError.isNotEmpty(),
                        supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            viewModel.userPassword.value = it
                            if (passwordError.isNotEmpty()) viewModel.passwordError.value = ""
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        isError = passwordError.isNotEmpty(),
                        supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff
                            IconButton(onClick = {
                                viewModel.passwordVisible.value = !passwordVisible
                            }) {
                                Icon(imageVector = icon, contentDescription = "Toggle Password")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            viewModel.confirmPassword.value = it
                            if (confirmPasswordError.isNotEmpty()) viewModel.confirmPasswordError.value = ""
                        },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        isError = confirmPasswordError.isNotEmpty(),
                        supportingText = { if (confirmPasswordError.isNotEmpty()) Text(confirmPasswordError) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (confirmPasswordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff
                            IconButton(onClick = {
                                viewModel.confirmPasswordVisible.value = !confirmPasswordVisible
                            }) {
                                Icon(imageVector = icon, contentDescription = "Toggle Password")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val context = LocalContext.current
                    Button(
                        onClick = {
                            if (viewModel.validate()) {
                                val user = User(userId, name, number, email, password)
                                Log.d("user", user.toString())
                                viewModel.getUserandSetStateByuserId(user.userId)
                                viewModel.createUserintable(user)
                                Log.d("Tag", "Created User with id $userId")
                                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            else {
                                Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Up")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Already have an account? Log In",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            navController.navigate("login"){
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
    }
}