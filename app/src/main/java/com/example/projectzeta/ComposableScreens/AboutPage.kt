package com.example.projectzeta.ComposableScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.Model.User
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.UserViewModel

@Composable
fun AboutPage(userViewModel: UserViewModel = viewModel(), navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {
        userViewModel.getUserandSetState(session.getLoggedInUser())
    }

    var enableChange by remember { mutableStateOf(false) }

    val userId by userViewModel.userId.collectAsState()
    val editName by userViewModel.nameOfUser.collectAsState()
    val editEmail by userViewModel.userEmail.collectAsState()
    val editMobileNumber by userViewModel.userPhoneNumber.collectAsState()
    val editPassword by userViewModel.userPassword.collectAsState()
    val passwordVisible by userViewModel.passwordVisible.collectAsState()
    val confirmPasswordVisible by userViewModel.confirmPasswordVisible.collectAsState()

    val nameError by userViewModel.nameError.collectAsState()
    val numberError by userViewModel.numberError.collectAsState()
    val emailError by userViewModel.emailError.collectAsState()
    val passwordError by userViewModel.passwordError.collectAsState()
    val confirmPasswordError by userViewModel.confirmPasswordError.collectAsState()

    val editConfirmPassword by userViewModel.confirmPassword.collectAsState()

    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    var footerindex by remember { mutableStateOf(3) }

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp)
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 2.dp,
                    shadowElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logout),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    session.logout()
                                    navController.navigate("login") {
                                        popUpTo("homeScreen") { inclusive = true }
                                    }
                                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Logout",
                            modifier = Modifier.clickable {
                                session.logout()
                                navController.navigate("login") {
                                    popUpTo("homeScreen") { inclusive = true }
                                }
                                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                            },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 20.dp),
                            fontWeight = FontWeight.SemiBold
                        )

                        Image(
                            painter = painterResource(R.drawable.outline_person_24),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            "Edit Profile",
                            modifier = Modifier.clickable { enableChange = true },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            enabled = enableChange,
                            value = editName,
                            onValueChange = { userViewModel.nameOfUser.value = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nameError.isNotEmpty(),
                            supportingText = { if (nameError.isNotEmpty()) Text(nameError) }
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            enabled = enableChange,
                            value = editMobileNumber,
                            onValueChange = { userViewModel.userPhoneNumber.value = it.filter { it.isDigit() } },
                            label = { Text("Mobile Number") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = numberError.isNotEmpty(),
                            supportingText = { if (numberError.isNotEmpty()) Text(numberError) }
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            enabled = enableChange,
                            value = editEmail,
                            onValueChange = { userViewModel.userEmail.value = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = emailError.isNotEmpty(),
                            supportingText = { if (emailError.isNotEmpty()) Text(emailError) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            enabled = enableChange,
                            value = editPassword,
                            onValueChange = { userViewModel.userPassword.value = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { userViewModel.passwordVisible.value = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            enabled = enableChange,
                            value = editConfirmPassword,
                            onValueChange = { userViewModel.confirmPassword.value = it },
                            label = { Text("Confirm Password") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { userViewModel.confirmPasswordVisible.value = !confirmPasswordVisible }) {
                                    Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                enabled = enableChange,
                                onClick = {
                                    if (userViewModel.validate()) {
                                        userViewModel.updateUser(User(userId, editName, editMobileNumber, editEmail, editPassword))
                                        session.logout()
                                        navController.navigate("login") { popUpTo("homeScreen") { inclusive = true } }
                                        Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Text("Save changes")
                            }

                            Button(
                                onClick = {
                                    userViewModel.deleteUser()
                                    session.logout()
                                    navController.navigate("signupScreen") { popUpTo(0) }
                                }
                            ) {
                                Text("Delete Account")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                tonalElevation = 8.dp
            ) {
                footerr.forEachIndexed { index, icon ->
                    NavigationBarItem(
                        selected = footerindex == index,
                        onClick = {
                            footerindex = index
                            val route = when(index) {
                                0 -> "homeScreen"
                                1 -> "lostAndFoundScreen"
                                2 -> "parkingScreen"
                                else -> "aboutPage"
                            }
                            navController.navigate(route)
                        },
                        icon = { Icon(painterResource(icon), contentDescription = null) },
                        label = {
                            val label = when(index) {
                                0 -> "Home" 1 -> "Lost & Found" 2 -> "Parking" else -> "Profile"
                            }
                            Text(label, fontSize = 12.sp)
                        }
                    )
                }
            }
        }
    }
}
