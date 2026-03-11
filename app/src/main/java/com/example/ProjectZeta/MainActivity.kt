package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.ProjectZeta.model.Notice

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    HomeScreen()
                    SignUpScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFBCBFE8)).padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Handle profile click */ }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile",
                        modifier = Modifier.height(48.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hello, Janai Kasle!", // Replace with actual user name
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Campus Notice Board", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Information Carousel
        val notices = listOf(
            Notice("New Resource", "Deep Learning notes uploaded by Jane Smith"),
            Notice("Lost Item", "Blue backpack found in the cafeteria. Contact security."),
            Notice("Parking Finder", "Available slots: P1-10, P2-5, P3-12")
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(notices) { notice ->
                Card(
                    modifier = Modifier
                        .width(280.dp) // Fixed width for cards in LazyRow
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = notice.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notice.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(52.dp))

        // Action Buttons
        Button(
            onClick = {  }, //navigation to LNS Screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LNS (Live Note Sharing)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { }, //upload notes screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Notes")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  }, //find notes screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Find Notes")
        }
    }
}

@Composable
fun SignUpScreen() {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Error states
    var nameError by remember { mutableStateOf<String?>(null) }
    var numberError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var ok = true

        // --- Name ---
        val nameTrim = name.trim()
        val nameRegex = Regex("^[A-Za-z][A-Za-z\\s'’-]{1,49}$")
        nameError = when {
            nameTrim.isEmpty() -> { ok = false; "Name is required" }
            !nameRegex.matches(nameTrim) -> { ok = false; "Use 2–50 letters; spaces, apostrophes, hyphens allowed" }
            else -> null
        }

        // --- Phone Number ---
        val digitsOnly = number.filter { it.isDigit() }
        val phoneRegex = Regex("^[6-9]\\d{9}$") // Starts with 6-9 and has exactly 10 digits

        numberError = when {
            digitsOnly.isEmpty() -> { ok = false; "Phone number is required" }
            digitsOnly.length != 10 -> { ok = false; "Must be exactly 10 digits" }
            !phoneRegex.matches(digitsOnly) -> { ok = false; "Invalid mobile format (must start with 6-9)" }
            else -> null
        }

        // --- Email ---
        val emailTrim = email.trim()
        emailError = when {
            emailTrim.isEmpty() -> { ok = false; "Email is required" }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches() -> {
                ok = false; "Enter a valid email address"
            }
            else -> null
        }

        // --- Password ---
        val pw = password
        if (pw.isEmpty()) {
            passwordError = "Password is required"
            ok = false
        } else {
            val rules = listOf(
                Regex(".{8,}") to "8+ characters",
                Regex("[a-z]") to "one lowercase",
                Regex("[A-Z]") to "one uppercase",
                Regex("\\d") to "one digit",
                Regex("[^A-Za-z0-9]") to "one special character"
            )
            val failed = rules.filter { (rx, _) -> !rx.containsMatchIn(pw) }.map { it.second }
            passwordError = if (failed.isNotEmpty()) {
                ok = false
                "Password must include: ${failed.joinToString(", ")}"
            } else null
        }

        return ok
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize().background(color = Color(0xFFBCBFE8))
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Create Account", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (nameError != null) nameError = null // clear as user types
                },
                label = { Text("Name") },
                singleLine = true,
                isError = nameError != null,
                supportingText = { if (nameError != null) Text(nameError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Phone
            OutlinedTextField(
                value = number,
                onValueChange = {
                    number = it
                    if (numberError != null) numberError = null
                },
                label = { Text("Phone Number") },
                singleLine = true,
                isError = numberError != null,
                supportingText = { if (numberError != null) Text(numberError!!) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) emailError = null
                },
                label = { Text("Email Address") },
                singleLine = true,
                isError = emailError != null,
                supportingText = { if (emailError != null) Text(emailError!!) },
                // This provides the @ and . buttons on the keyboard
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                label = { Text("Password") },
                singleLine = true,
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) Text(passwordError!!)
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current
            Button(
                onClick = {
                    if (validate()) {
                        Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        // Proceed to next screen (Login)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {  }) {
                Text("Already have an account? Log In")
            }
        }
    }
}


