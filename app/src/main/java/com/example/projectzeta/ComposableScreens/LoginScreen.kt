package com.example.projectzeta.ComposableScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.UserViewModel

@Composable
fun LoginScreen(userViewModel: UserViewModel = viewModel(), navController: NavController) {

    val context = LocalContext.current
    val session = SessionManager(context)
    userViewModel.getUserandSetState(session.getLoggedInUser())

    val passwordVisible by userViewModel.passwordVisible.collectAsState()

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
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
            ) {
                val userPhoneNumber by userViewModel.userPhoneNumber.collectAsState()
                val userPassword by userViewModel.userPassword.collectAsState()
                var loginUserPhone by remember { mutableStateOf("") }
                var loginPassword by remember { mutableStateOf("") }
                val context =LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "CAMPUS CONNECT",
                        modifier = Modifier.padding(bottom = 32.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = loginUserPhone,
                        onValueChange = {loginUserPhone=it },
                        label = { Text("Enter Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text("Enter Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff
                            IconButton(onClick = {
                                userViewModel.passwordVisible.value = !passwordVisible
                            }) {
                                Icon(imageVector = icon, contentDescription = "Toggle Password")
                            }
                        }
                    )

                    Row(Modifier.padding(top = 20.dp)) {
                        Text(
                            "Sign Up",
                            Modifier
                                .padding(start = 15.dp)
                                .clickable(onClick = {
                                    navController.navigate("signupScreen")
                                }),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Forgot Password",
                            Modifier.padding(start = 40.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    userViewModel.getUserandSetState(loginUserPhone)
                    Button(
                        onClick = {
                            if((userPhoneNumber==loginUserPhone) && (userPassword==loginPassword)){
                                Log.d("Tag","Login Successful")
                                session.saveLogin(loginUserPhone)
                                navController.navigate("homeScreen"){
                                    popUpTo("login"){inclusive=true}
                                }
                            }
                            else{
                                Log.d("Tag","Phone Number and Password does not match!")
                                Toast.makeText(context, "Phone Number and Password does not match!", Toast.LENGTH_LONG).show()
                            }

                        },
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}
