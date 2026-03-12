package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ProjectZeta.model.Notice
import androidx.compose.ui.unit.sp
import com.example.ProjectZeta.model.Found
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.projectzeta.Model.ParkingSlot
import com.example.projectzeta.ViewModels.ReservationViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.get

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    parkingScreen()
//                    HomeScreen()
//                    SignUpScreen()
                    LostAndFoundScreen()
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

@Composable
fun parkingScreen(viewModel: ReservationViewModel = viewModel()) {
    val slots by viewModel.slots.collectAsState()
    val currentUser = "Fuzail" //will be the current users username

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Column {
            Text("Campus Connect Header here", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
        Column(Modifier.weight(1f)) {
            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(slots, key = {it.parkingId}){slot->
                    var color = if(slot.available) Color.Green else Color.Red
                    Column(
                        Modifier.padding(8.dp)
                            .height(65.dp)
                            .background(color)
                            .clickable{
                                viewModel.reserveWithDb(slot, currentUser)
                                if(slot.available){
                                    color = Color.Green
                                } else{
                                    color = Color.Red
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(Modifier.height(2.dp))
                        Icon(painterResource(R.drawable.parking_icon), contentDescription = "ParkingIcon", Modifier.size(35.dp), tint = Color.Magenta)
                        Spacer(Modifier.height(2.dp))
                        Text("Id: ${slot.parkingId}", color = Color.White)
                    }

                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Column(Modifier.height(250.dp).padding(8.dp, 0.dp)) {
            Text("Total Slots: ${slots.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Column{
                    Text("Available Slots: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    LazyColumn {
                        items(slots.size){it->
                            if(slots[it].available)
                                Text(slots[it].parkingId.toString())
                        }
                    }
                }
                Spacer(Modifier.width(50.dp))
                Column{
                    Text("Reserved Slots: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    LazyColumn {
                        items(slots.size){it->
                            if(!slots[it].available)
                                Text(slots[it].parkingId.toString())
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Column { Text("Footer Comes Here") }
    }
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

@Composable
fun LostAndFoundScreen() {

    var search by remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }
    val tabtitles = listOf("Lost", "Found")
//    val footer=listOf("Home","LNF","PF","About")
    var footerindex by remember { mutableStateOf(0) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )

    var foundindex by remember { mutableStateOf(0) }

    var list2 = mutableListOf<Found>()
    list2.add(Found("Title 1", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 2", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 3", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 4", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 5", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 6", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 7", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 8", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 9", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 10", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 11", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 12", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 13", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 14", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 15", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 16", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 17", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 18", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 19", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 20", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 21", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 22", "Description 1", R.drawable.ic_launcher_background))
    list2.add(Found("Title 23", "Description 1", R.drawable.ic_launcher_background))

    var listt = mutableListOf<String>()
    listt.add("Description 1")
    listt.add("Description 2")
    listt.add("Description 3")
    listt.add("Description 4")
    listt.add("Description 5")
    listt.add("Description 6")
    listt.add("Description 7")
    listt.add("Description 8")
    listt.add("Description 9")
    listt.add("Description 10")
    listt.add("Description 11")
    listt.add("Description 12")
    listt.add("Description 13")
    listt.add("Description 14")
    listt.add("Description 15")
    listt.add("Description 16")
    listt.add("Description 17")
    listt.add("Description 18")
    listt.add("Description 19")
    listt.add("Description 20")
    listt.add("Description 21")
    listt.add("Description 22")

    Column(modifier = Modifier.fillMaxWidth()) {

        TabRow(
            selectedTabIndex = selectedtabindex,
            divider = { HorizontalDivider() },
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
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
            tabtitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedtabindex == index,
                    onClick = { selectedtabindex = index },
                    text = { Text(text = title) }
                )
            }
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            Modifier.fillMaxWidth().padding(top = 20.dp),
            label = { Text("Search") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
            }
        )
        Card(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth().padding(top = 20.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedtabindex) {
                    0 -> {
                        items(listt) { item ->
                            Text(item, modifier = Modifier.padding(vertical = 8.dp))
                            HorizontalDivider()
                        }
                    }

                    1 -> {
                        items(
                            items = list2,
                            key = { it.hashCode() }
                        ) { found ->
                            val (foundTitle, foundDesc, foundImage) = found

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { foundindex = list2.indexOf(found) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = foundImage),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = foundTitle,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = foundDesc,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth().padding(top = 20.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }

        TabRow(
            selectedTabIndex = footerindex,
            divider = { HorizontalDivider() },
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(top = 130.dp)
                .fillMaxWidth().size(100.dp)
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
                        if (index == 1) {
                            selectedtabindex = 0
                        }
                    },
                    icon = {
                        Image(
                            painterResource(icon),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun Login(){
    val username by rememberSaveable { mutableStateOf("") }
    val password by rememberSaveable { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("CAMPUS CONNECT", Modifier.padding(bottom = 100.dp),fontWeight = FontWeight.Bold, fontSize = 30.sp)
        OutlinedTextField(
            value = username,
            onValueChange = {username},
            label = { Text("Enter Username") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password},
            label = { Text("Enter Password") }
        )
        Row(Modifier.padding(top = 20.dp)) {
            Text("Sign Up", Modifier.padding(start = 15.dp))
            Text("Forgot Password", Modifier.padding(start = 40.dp))
        }
        Button(onClick = {}, Modifier.padding(top = 20.dp)) {Text("Login") }
    }
}
