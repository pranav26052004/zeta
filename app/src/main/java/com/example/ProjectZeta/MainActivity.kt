package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
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

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.HorizontalDivider


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
    var footerr : List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    var footerindex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        TabRow(
            selectedTabIndex = selectedtabindex,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            painterResource(icon) ,
                            contentDescription = null
                        )
                    }
                )
            }


        }
    }
}


@Composable
fun SignUpScreen(viewModel: UserViewModel = viewModel(), navController: NavController) {
    val name by viewModel.nameOfUser.collectAsState()
    val number by viewModel.userPhoneNumber.collectAsState()
    val email by viewModel.userEmail.collectAsState()
    val password by viewModel.userPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

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
                        value = name,
                        onValueChange = { viewModel.nameOfUser.value = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = number,
                        onValueChange = {
                            viewModel.userPhoneNumber.value = it.filter { c -> c.isDigit() }
                        },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.userEmail.value = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.userPassword.value = it },
                        label = { Text("Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { viewModel.confirmPassword.value = it },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            navController.navigate("login")
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
                            navController.navigate("login")
                        }
                    )
                }
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
fun RadialGlowBackground(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0x335B6DFA), // ~20% alpha indigo glow
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .drawBehind {
                // Top-left gentle glow
                drawCircle(
                    color = glowColor,
                    radius = size.minDimension * 0.6f,
                    center = Offset(x = size.width * 0.25f, y = size.height * 0.15f)
                )

                drawCircle(
                    color = glowColor.copy(alpha = 0.18f),
                    radius = size.minDimension * 0.45f,
                    center = Offset(x = size.width * 0.85f, y = size.height * 0.25f)
                )
                // Bottom faint glow
                drawCircle(
                    color = glowColor.copy(alpha = 0.12f),
                    radius = size.minDimension * 0.5f,
                    center = Offset(x = size.width * 0.5f, y = size.height * 0.95f)
                )
            }
    ) {
        content()
    }
}



@Composable
fun Login(userViewModel: UserViewModel = viewModel(), navController: NavController) {

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

                val userEmail by userViewModel.userEmail.collectAsState()
                val userPassword by userViewModel.userPassword.collectAsState()

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
                        value = userEmail,
                        onValueChange = { userViewModel.userEmail.value = it },
                        label = { Text("Enter Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userPassword,
                        // NOTE: fixed bug here — now updates the state
                        onValueChange = { userViewModel.userPassword.value = it },
                        label = { Text("Enter Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
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
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate("homeScreen")
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

    var footerr : List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
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
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
fun UploadNotes(navController:NavController){
    var selectedtabindex by remember { mutableStateOf(0) }
    var footerr : List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
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
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                painterResource(icon),
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

//@Composable
//fun LiveNotesSharing(viewModels: LiveNotesSharing,navController: NavController) {
//    var selectedtabindex by remember { mutableStateOf(0) }
//    var footerr : List<Int> = listOf(
//        R.drawable.baseline_home_24,
//        R.drawable.outline_feature_search_24,
//        R.drawable.baseline_local_parking_24,
//        R.drawable.outline_person_24
//    )
//    val footerindex by viewModels.footerindex.collectAsState()
//    val selectedTab by viewModels.selectedTab.collectAsState()
//    val searchQuery by viewModels.searchQuery.collectAsState()
//    val searchTitle by viewModels.searchTitle.collectAsState()
//    val searchLiveText by viewModels.searchLiveText.collectAsState()
//    val goLiveTitle by viewModels.goLiveTitle.collectAsState()
//    val goLiveDescription by viewModels.goLiveDescription.collectAsState()
//    val liveId by viewModels.goLiveDescription.collectAsState()
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Live Screen",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp)
//        )
//        val tabs = listOf("Search", "Go Live")
//        TabRow(selectedTabIndex = selectedTab) {
//            tabs.forEachIndexed { index, title ->
//                Tab(
//                    selected = selectedTab == index,
//                    onClick = { viewModels.selectedTab.value = index },
//                    text = { Text(title) }
//                )
//            }
//        }
//        Spacer(Modifier.height(16.dp))
//        if (selectedTab == 0) {
//            Column(modifier = Modifier.fillMaxWidth()) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = { viewModels.searchQuery.value = it },
//                    label = { Text("Search") },
//                    placeholder = { Text("Search link...") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(12.dp))
//                Button(onClick = {
//                }, modifier = Modifier.padding(start=140.dp)) {
//                    Text("Search")
//                }
//                Spacer(Modifier.height(12.dp))
//                Card(elevation= CardDefaults.cardElevation(10.dp), colors =  CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
//                   Spacer(Modifier.height(5.dp))
//                    Text(
//                        searchTitle,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(Modifier.height(5.dp))
//                }
//                Spacer(Modifier.height(12.dp))
//                Card(elevation= CardDefaults.cardElevation(10.dp), colors =  CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
//                    Text(
//                        searchLiveText,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(500.dp)
//                    )
//                }
//            }
//        } else {
//            Column(modifier = Modifier.fillMaxWidth()) {
//                OutlinedTextField(
//                    value = goLiveTitle,
//                    onValueChange = { viewModels.goLiveTitle.value = it },
//                    label = { Text("Live Title") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(12.dp))
//                OutlinedTextField(
//                    value = goLiveDescription,
//                    onValueChange = { viewModels.goLiveDescription.value = it },
//                    label = { Text("Description") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(600.dp)
//                )
//                Spacer(Modifier.height(12.dp))
//                Text(liveId)
//
//            }
//        }
//    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Bottom
//    ) {
//        TabRow(
//            selectedTabIndex = selectedtabindex,
//            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
//            divider = { HorizontalDivider() },
//            modifier = Modifier
////                .padding(top = 150.dp)
//                .fillMaxWidth()
//                .drawBehind {
//                    val x = size.width / 2f
//                    drawLine(
//                        color = Color.Black,
//                        start = Offset(x, 0f),
//                        end = Offset(x, size.height),
//                        strokeWidth = 2.dp.toPx()
//                    )
//                }
//        ) {
//            footerr.forEachIndexed { index, icon ->
//                Tab(
//
//                    selected = footerindex == index,
//                    onClick = {
//                        viewModels.footerindex.value = index
//                        if (index==1) {
//                            selectedtabindex = 0
//                        }
//                    },
//                    icon = {
//                        Icon(
//                            painterResource(icon),
//                            contentDescription = null
//                        )
//                    }
//                )
//            }
//
//
//        }
//    }
//}

@Composable
fun LiveNotesSharing(viewModels: LiveNotesSharing, navController: NavController) {
    var selectedtabindex by remember { mutableStateOf(0) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )

    // Existing states from your ViewModel (kept exactly as you had them)
    val footerindex by viewModels.footerindex.collectAsState()
    val selectedTab by viewModels.selectedTab.collectAsState()
    val searchQuery by viewModels.searchQuery.collectAsState()
    val searchTitle by viewModels.searchTitle.collectAsState()
    val searchLiveText by viewModels.searchLiveText.collectAsState()
    val goLiveTitle by viewModels.goLiveTitle.collectAsState()
    val goLiveDescription by viewModels.goLiveDescription.collectAsState()
    val liveId by viewModels.goLiveDescription.collectAsState() // kept as-is (your original line)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Live Screen",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Top tabs (Search / Go Live)
        val tabs = listOf("Search", "Go Live")
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { positions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(positions[selectedTab]),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            },
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .padding(bottom = 12.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { viewModels.selectedTab.value = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Main content card (polished surface)
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (selectedTab == 0) {
                // -------------------- SEARCH TAB --------------------
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModels.searchQuery.value = it },
                        label = { Text("Search") },
                        placeholder = { Text("Search link...") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    // Inline centered Search button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { /* keep your original empty action */ },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Search")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Title card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = searchTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Live text content area
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surface),
                        ) {
                            Text(
                                text = searchLiveText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            )
                        }
                    }
                }
            } else {
                // -------------------- GO LIVE TAB --------------------
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = goLiveTitle,
                        onValueChange = { viewModels.goLiveTitle.value = it },
                        label = { Text("Live Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    // Big description field
                    OutlinedTextField(
                        value = goLiveDescription,
                        onValueChange = { viewModels.goLiveDescription.value = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // more ergonomic height
                    )

                    Spacer(Modifier.height(12.dp))

                    // LiveId text (kept same behavior)
                    Text(
                        text = liveId,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))

                    // Action hint area (visual polish; no logic change)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                text = "Tip: Share your live link or title so others can find it in Search.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // -------------------- Bottom Footer Tabs (unchanged logic, modernized visuals) --------------------
        TabRow(
            selectedTabIndex = selectedtabindex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicator = { positions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(positions[footerindex.coerceIn(0, positions.lastIndex)]),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            divider = {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .drawBehind {
                    // subtle center divider (kept from your style)
                    val x = size.width / 2f
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.35f), // safe color, no theme mismatch
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            footerr.forEachIndexed { index, icon ->
                Tab(
                    selected = footerindex == index,
                    onClick = {
                        viewModels.footerindex.value = index
                        if (index == 1) {
                            selectedtabindex = 0
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = if (footerindex == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
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


