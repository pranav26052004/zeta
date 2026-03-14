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

    var enableChange by remember{mutableStateOf(false)}

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

    val editConfirmPassword by userViewModel.confirmPassword.collectAsState()//remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }


    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    var footerindex by remember { mutableStateOf(0) }

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            bottomBar = {
                Surface(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    tonalElevation = 2.dp,
                    shadowElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                ) {
                    TabRow(
                        selectedTabIndex = selectedtabindex,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        divider = { HorizontalDivider() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .drawBehind {
                                val x = size.width / 2f
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                    ) {
                        footerr.forEachIndexed { index, icon ->
                            Tab(
                                selected = footerindex == index,
                                onClick = {
                                    footerindex = index
                                    if (index == 0) {
                                        Log.d("tag", "Clicking $index")
                                        navController.navigate("homeScreen")
                                    }
                                    if (index == 1) {
                                        Log.d("tag", "Clicking $index")
                                        navController.navigate("lostAndFoundScreen")
                                    }
                                    if (index == 2) {
                                        Log.d("tag", "Clicking $index")
                                        navController.navigate("parkingScreen")
                                    }
                                    if (index == 3) {
                                        Log.d("tag", "Clicking $index")
                                        navController.navigate("aboutPage")
                                    }
                                },
                                icon = {
                                    Icon(
                                        painterResource(icon),
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logout bar
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
                                .clickable(
                                    true, onClick = {
                                        navController.navigate("login")
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Logout",
                            modifier = Modifier.clickable {
                                session.logout()
                                navController.navigate("login"){
                                    popUpTo("homeScreen") {inclusive = true}
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

                // Main Profile Surface
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
                            .verticalScroll(rememberScrollState())
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
                            modifier = Modifier.clickable {
                                enableChange = true
                                Log.d("ClickableText", "Text clicked!")
                            },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Name
                        OutlinedTextField(
                            enabled = enableChange,
                            value = editName,
                            onValueChange = {
                                userViewModel.nameOfUser.value = it
                                if (nameError.isNotEmpty()) userViewModel.nameError.value = ""
                            },
                            label = { Text("Name") },
                            singleLine = true,
                            isError = nameError.isNotEmpty(),
                            supportingText = { if (nameError.isNotEmpty()) Text(nameError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        // Mobile Number
                        OutlinedTextField(
                            enabled = enableChange,
                            value = editMobileNumber,
                            onValueChange = {
                                userViewModel.userPhoneNumber.value = it.filter { c -> c.isDigit() }
                                if (numberError.isNotEmpty()) userViewModel.numberError.value = ""
                            },
                            label = { Text("Mobile Number") },
                            singleLine = true,
                            isError = numberError.isNotEmpty(),
                            supportingText = { if (numberError.isNotEmpty()) Text(numberError) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        // Email
                        OutlinedTextField(
                            enabled = enableChange,
                            value = editEmail,
                            onValueChange = {
                                userViewModel.userEmail.value = it
                                if (emailError.isNotEmpty()) userViewModel.emailError.value = ""
                            },
                            label = { Text("Email") },
                            singleLine = true,
                            isError = emailError.isNotEmpty(),
                            supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password
                        OutlinedTextField(
                            enabled = enableChange,
                            value = editPassword,
                            onValueChange = {
                                userViewModel.userPassword.value = it
                                if (passwordError.isNotEmpty()) userViewModel.passwordError.value = ""
                            },
                            label = { Text("Password") },
                            singleLine = true,
                            isError = passwordError.isNotEmpty(),
                            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // Confirm Password
                        OutlinedTextField(
                            enabled = enableChange,
                            value = editConfirmPassword,
                            onValueChange = {
                                userViewModel.confirmPassword.value = it
                                if (confirmPasswordError.isNotEmpty()) userViewModel.confirmPasswordError.value = ""
                            },
                            label = { Text("Confirm Password") },
                            singleLine = true,
                            isError = confirmPasswordError.isNotEmpty(),
                            supportingText = { if (confirmPasswordError.isNotEmpty()) Text(confirmPasswordError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon =
                                    if (confirmPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff

                                IconButton(
                                    onClick = { userViewModel.confirmPasswordVisible.value = !confirmPasswordVisible }
                                ) {
                                    Icon(imageVector = icon, contentDescription = "Toggle Password")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                enabled = enableChange,
                                onClick = {
                                    if (userViewModel.validate()) {
                                        Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show()

                                        userViewModel.updateUser(
                                            User(
                                                userId,
                                                editName,
                                                editMobileNumber,
                                                editEmail,
                                                editPassword
                                            )
                                        )
                                        session.logout()
                                        navController.navigate("login"){
                                            popUpTo("homeScreen") {inclusive = true}
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please enter valid details",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text("Save changes")
                            }

                            Button(
                                onClick = {
                                    userViewModel.deleteUser()
                                    session.logout()
                                    navController.navigate("signupScreen"){
                                        popUpTo(0)
                                    }
                                },
                                modifier = Modifier.padding(start = 30.dp)
                            ) {
                                Text("Delete Account")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
