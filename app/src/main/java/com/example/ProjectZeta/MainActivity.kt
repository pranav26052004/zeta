package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ProjectZeta.ViewModels.LiveNotesSharing
import com.example.ProjectZeta.model.Notice
import com.example.ProjectZeta.ViewModels.UserViewModel
import com.example.ProjectZeta.constants.FirebaseDatabases
import com.example.ProjectZeta.model.Found
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.projectzeta.Model.User
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import com.example.projectzeta.ViewModels.ReservationViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    parkingScreen()
//                    HomeScreen()
//                    SignUpScreen()
//                    LostAndFoundScreen()
//                    FindAllNotes()
                   AppNavigation()
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
fun parkingScreen(viewModel: ReservationViewModel = viewModel(),navController: NavController) {
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
        AppNavigation()
    }
}

@Composable
fun HomeScreen(viewModels: LiveNotesSharing, navController:NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFBCBFE8)).padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigate("aboutPage") }) {
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
            onClick = {
                navController.navigate("liveNotesSharing")
            }, //navigation to LNS Screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LNS (Live Note Sharing)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("uploadNotes")
            }, //upload notes screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Notes")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("findAllNotes")
            }, //find notes screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Find Notes")
        }
    }
    var selectedtabindex by remember { mutableStateOf(0) }
    var footerr : List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.ShoppingCart,
        Icons.Default.AccountCircle,
    )
    var footerindex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        TabRow(
            selectedTabIndex = selectedtabindex,
            divider = { HorizontalDivider() },
            modifier = Modifier
//                .padding(top = 150.dp)
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
            footerr.forEachIndexed { index, icon ->
                Tab(
                    selected = footerindex == index,
                    onClick = {
                        viewModels.footerindex.value = index
                        if (index==1) {
                            selectedtabindex = 0
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                )
            }


        }
    }
}

@Composable
fun SignUpScreen(viewModel: UserViewModel = viewModel(),navController:NavController) {

    val name by viewModel.nameOfUser.collectAsState()
    val number by viewModel.userPhoneNumber.collectAsState()
    val email by viewModel.userEmail.collectAsState()
    val password by viewModel.userPassword.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val numberError by viewModel.numberError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()

    fun validate(): Boolean {
        var ok = true

        // Name
        val nameTrim = name.trim()
        val nameRegex = Regex("^[A-Za-z][A-Za-z\\s''-]{1,49}$")
        viewModel.nameError.value = when {
            nameTrim.isEmpty() -> { ok = false; "Name is required" }
            !nameRegex.matches(nameTrim) -> { ok = false; "Use 2–50 letters; spaces, apostrophes, hyphens allowed" }
            else -> ""
        }

        // Phone
        val digitsOnly = number.filter { it.isDigit() }
        val phoneRegex = Regex("^[6-9]\\d{9}$")
        viewModel.numberError.value = when {
            digitsOnly.isEmpty() -> { ok = false; "Phone number is required" }
            digitsOnly.length != 10 -> { ok = false; "Must be exactly 10 digits" }
            !phoneRegex.matches(digitsOnly) -> { ok = false; "Invalid mobile format (must start with 6-9)" }
            else -> ""
        }

        // Email
        val emailTrim = email.trim()
        viewModel.emailError.value = when {
            emailTrim.isEmpty() -> { ok = false; "Email is required" }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches() -> {
                ok = false; "Enter a valid email address"
            }
            else -> ""
        }

        // Password
        val pw = password
        if (pw.isEmpty()) {
            viewModel.passwordError.value = "Password is required"
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
            viewModel.passwordError.value = if (failed.isNotEmpty()) {
                ok = false; "Password must include: ${failed.joinToString(", ")}"
            } else ""
        }

        // Confirm Password
        val confirmPw = confirmPassword
        viewModel.confirmPasswordError.value = when {
            confirmPw.isEmpty() -> { ok = false; "Please confirm your password" }
            confirmPw != password -> { ok = false; "Passwords do not match" }
            else -> ""
        }

        return ok
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE1E6FF))
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
                    viewModel.nameOfUser.value = it
                    if (nameError.isNotEmpty()) viewModel.nameError.value = ""
                },
                label = { Text("Name") },
                singleLine = true,
                isError = nameError.isNotEmpty(),
                supportingText = { if (nameError.isNotEmpty()) Text(nameError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Phone
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
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    viewModel.userEmail.value = it
                    if (emailError.isNotEmpty()) viewModel.emailError.value = ""
                },
                label = { Text("Email Address") },
                singleLine = true,
                isError = emailError.isNotEmpty(),
                supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            val context = LocalContext.current
            Button(
                onClick = {
                    if (validate()) {
                        Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    }
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate("login")
            }) {
                Text("Already have an account? Log In")
            }
        }
    }
}

@Composable
fun LostAndFoundScreen(navController:NavController) {

    var search by remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }
    val tabtitles = listOf("Lost", "Found")
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
fun Login(userViewModel: UserViewModel = viewModel(),navController:NavController){
    val userEmail by userViewModel.userEmail.collectAsState()
    val userPassword by userViewModel.userPassword.collectAsState()
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("CAMPUS CONNECT", Modifier.padding(bottom = 100.dp),fontWeight = FontWeight.Bold, fontSize = 30.sp)
        OutlinedTextField(
            value = userEmail,
            onValueChange = {userViewModel.userEmail.value = it},
            label = { Text("Enter Username") }
        )
        OutlinedTextField(
            value = userPassword,
            onValueChange = {userPassword},
            label = { Text("Enter Password") }
        )
        Row(Modifier.padding(top = 20.dp)) {
            Text("Sign Up", Modifier.padding(start = 15.dp).clickable(true, onClick = {
                navController.navigate("signupScreen")
            }))
            Text("Forgot Password", Modifier.padding(start = 40.dp))
        }
        Button(onClick = {
            navController.navigate("homeScreen")
        }, Modifier.padding(top = 20.dp)) {Text("Login") }
    }
}

@Composable
fun AboutPage(userViewModel: UserViewModel = viewModel(),navController:NavController) {
    var context=LocalContext.current
    userViewModel.getUserandSetState("412345")
    val editName by userViewModel.nameOfUser.collectAsState() //remember { mutableStateOf("Sumanth") }
    val editMobileNumber by userViewModel.userPhoneNumber.collectAsState() //remember { mutableStateOf("9876543210") }
    val editEmail by userViewModel.userEmail.collectAsState() //remember { mutableStateOf("Sumanth@gmail.com") }
    val editPassword by userViewModel.userPassword.collectAsState() //remember { mutableStateOf(("********")) }
    var editConfirmPassword by remember { mutableStateOf("") }
    val passwordVisible by userViewModel.passwordVisible.collectAsState()

    var selectedtabindex by remember { mutableStateOf(0) }

    var footerr : List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.ShoppingCart,
        Icons.Default.AccountCircle,
    )
    var footerindex by remember { mutableStateOf(0) }


    Column (
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.logout),
            modifier = Modifier.padding(start = 300.dp),
            contentDescription = null
        )

        Text("Logout",
            modifier = Modifier.padding(start = 300.dp).clickable {
                Toast.makeText(context,"Logged out", Toast.LENGTH_SHORT).show()
            })
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Image(
            painter = painterResource(R.drawable.outline_person_24),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text("Edit Profile",
            modifier = Modifier.clickable {
                // Handle the click action here, for example, logging a message or updating state
                Log.d("ClickableText", "Text clicked!")
            })

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value=editName,
            onValueChange = {userViewModel.nameOfUser.value=it},
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value=editMobileNumber,
            onValueChange = {userViewModel.userPhoneNumber.value = it},
            label = { Text("Mobile Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value=editEmail,
            onValueChange = {userViewModel.userEmail.value=it},
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value=editPassword,
            onValueChange = {userViewModel.userPassword.value=it},
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if(passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                IconButton(
                    onClick = {userViewModel.passwordVisible.value = !passwordVisible}
                ) { Icon(imageVector = icon, contentDescription = "Toggle Password") }
            }
        )
        OutlinedTextField(
            value=editConfirmPassword,
            onValueChange = {editConfirmPassword=it},
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if(passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                IconButton(
                    onClick = {userViewModel.passwordVisible.value = !passwordVisible}
                ) { Icon(imageVector = icon, contentDescription = "Toggle Password") }
            }
        )
        Spacer(modifier = Modifier.height(30.dp))


        Row(
            modifier = Modifier.padding(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = {
                    Toast.makeText(context,"Changes Saved", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Save changes")
            }

            Button(
                onClick = {},
                Modifier.padding(start = 30.dp)
            ){
                Text("Delete Account")
            }

        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        TabRow(
            selectedTabIndex = selectedtabindex,
            divider = { HorizontalDivider() },
            modifier = Modifier
//                .padding(top = 150.dp)
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
            footerr.forEachIndexed { index, icon ->
                Tab(

                    selected = footerindex == index,
                    onClick = {
                        footerindex = index
                        if (index==1) {
                            selectedtabindex = 0
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                )
            }


        }
    }
}


@Composable
fun UploadNotes(navController:NavController){
    var selectedtabindex by remember { mutableStateOf(0) }
    var footerr : List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.ShoppingCart,
        Icons.Default.AccountCircle,
    )
    var footerindex by remember { mutableStateOf(0) }
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        LazyColumn(modifier = Modifier.height(700.dp)) {
            stickyHeader {
                Text(
                    text = "Upload Notes",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(20.dp)
                )
            }
            items(100) { item ->
                Card {
                    Text("Item: $item", modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(15.dp))


            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Add,contentDescription = null)
            }

        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Card(elevation = CardDefaults.cardElevation(50.dp)) {
            TabRow(
                selectedTabIndex = selectedtabindex,
                divider = { HorizontalDivider() },
                modifier = Modifier
//                .padding(top = 150.dp)
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
                footerr.forEachIndexed { index, icon ->
                    Tab(

                        selected = footerindex == index,
                        onClick = {
                            footerindex = index
                            if (index==1) {
                                selectedtabindex = 0
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FindAllNotes(navController:NavController){
    var search by remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }
    var footerindex by remember { mutableStateOf(0) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            Modifier.fillMaxWidth().padding(top = 30.dp),
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
                .fillMaxWidth().padding(top = 30.dp)
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ){
            LazyColumn (
                modifier = Modifier.fillMaxSize()
            ){
                stickyHeader { Text("Find All the Notes Here!",
                    Modifier.padding(start = 70.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp) }

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
fun LiveNotesSharing(viewModels: LiveNotesSharing,navController: NavController) {
    var selectedtabindex by remember { mutableStateOf(0) }
    var footerr : List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.ShoppingCart,
        Icons.Default.AccountCircle,
    )
    val footerindex by viewModels.footerindex.collectAsState()
    val selectedTab by viewModels.selectedTab.collectAsState()
    val searchQuery by viewModels.searchQuery.collectAsState()
    val searchTitle by viewModels.searchTitle.collectAsState()
    val searchLiveText by viewModels.searchLiveText.collectAsState()
    val goLiveTitle by viewModels.goLiveTitle.collectAsState()
    val goLiveDescription by viewModels.goLiveDescription.collectAsState()
    val liveId by viewModels.goLiveDescription.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Live Screen",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        val tabs = listOf("Search", "Go Live")
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { viewModels.selectedTab.value = index },
                    text = { Text(title) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (selectedTab == 0) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModels.searchQuery.value = it },
                    label = { Text("Search") },
                    placeholder = { Text("Search link...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = {

                }, modifier = Modifier.padding(start=140.dp)) {
                    Text("Search")
                }
                Spacer(Modifier.height(12.dp))
                Card(elevation= CardDefaults.cardElevation(10.dp), colors =  CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
                   Spacer(Modifier.height(5.dp))
                    Text(
                        searchTitle,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(5.dp))
                }
                Spacer(Modifier.height(12.dp))
                Card(elevation= CardDefaults.cardElevation(10.dp), colors =  CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
                    Text(
                        searchLiveText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(500.dp)
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = goLiveTitle,
                    onValueChange = { viewModels.goLiveTitle.value = it },
                    label = { Text("Live Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = goLiveDescription,
                    onValueChange = { viewModels.goLiveDescription.value = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(liveId)

            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        TabRow(
            selectedTabIndex = selectedtabindex,
            divider = { HorizontalDivider() },
            modifier = Modifier
//                .padding(top = 150.dp)
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
            footerr.forEachIndexed { index, icon ->
                Tab(

                    selected = footerindex == index,
                    onClick = {
                        viewModels.footerindex.value = index
                        if (index==1) {
                            selectedtabindex = 0
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                )
            }


        }
    }
}

@Composable
fun AppNavigation(){
    val navController=rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Login(viewModel(),navController)
        }
        composable("signupScreen") {
            SignUpScreen(viewModel(),navController)
        }
        composable("homeScreen") {
            HomeScreen(viewModel(),navController)
        }
        composable("parkingScreen") {
            parkingScreen(viewModel(),navController)
        }
        composable("lostAndFoundScreen") {
            LostAndFoundScreen(navController)
        }
        composable("aboutPage") {
            AboutPage(viewModel(),navController)
        }
        composable("uploadNotes") {
            UploadNotes(navController)
        }
        composable("findAllNotes") {
            FindAllNotes(navController)
        }
        composable ("liveNotesSharing"){
            LiveNotesSharing(viewModel(),navController)
        }
    }
}


