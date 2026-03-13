package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Search
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
import com.example.projectzeta.ViewModels.LiveNotesSharingViewModel
import com.example.projectzeta.model.Notice
import com.example.projectzeta.ViewModels.UserViewModel
import com.example.projectzeta.model.Found
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.projectzeta.Model.User
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.FoundViewModel
import com.example.projectzeta.ViewModels.LostViewModel
import com.example.projectzeta.ViewModels.ReservationViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val session = SessionManager(this)
            val loggedUser = session.getLoggedInUser()

            val startDestination = if(loggedUser!=null) "homeScreen" else "login"
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   AppNavigation(startDestination)
                }
            }
        }
    }
}

@Composable
fun parkingScreen(viewModel: ReservationViewModel = viewModel(),navController: NavController) {

    val slots by viewModel.slots.collectAsState()
    val userViewModel: UserViewModel = viewModel()
    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())

    var footerindex by remember { mutableStateOf(0) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text("Parking Area", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Column(Modifier.weight(1f)) {
                LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                    items(slots, key = { it.parkingId }) { slot ->
                        var color = if (slot.available) Color.Green else Color.Red
                        Column(
                            Modifier
                                .padding(8.dp)
                                .height(65.dp)
                                .background(color)
                                .clickable {
                                    viewModel.reserveWithDb(slot, userViewModel.nameOfUser.value)
                                    if (slot.available) {
                                        color = Color.Green
                                    } else {
                                        color = Color.Red
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(2.dp))
                            Icon(
                                painterResource(R.drawable.parking_icon),
                                contentDescription = "ParkingIcon",
                                Modifier.size(35.dp),
                                tint = Color.Magenta
                            )
                            Spacer(Modifier.height(2.dp))
                            Text("Slot: ${slot.parkingId}", color = Color.White)
                        }
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Column(
                Modifier
                    .height(250.dp)
                    .padding(8.dp, 0.dp)
            ) {
                Text("Total Slots: ${slots.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Card(
                        elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Column {
                            Text(
                                "Available Slots: ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            LazyColumn {
                                items(slots.size) { it ->
                                    if (slots[it].available)
                                        Text(slots[it].parkingId.toString())
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(50.dp))
                    Column {
                        Card(
                            elevation = CardDefaults.cardElevation(20.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text("Reserved Slots: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            LazyColumn {
                                items(slots.size) { it ->
                                    if (!slots[it].available)
                                        Text(slots[it].parkingId.toString())
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Column() {
                TabRow(
                    selectedTabIndex = footerindex,
                    divider = { HorizontalDivider() },
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth()
                        .size(100.dp)
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
//        AppNavigation()
    }
}

@Composable
fun HomeScreen(viewModels: LiveNotesSharingViewModel, navController: NavController) {
    val userViewModel: UserViewModel=viewModel()
    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())
    val userName by userViewModel.nameOfUser.collectAsState()
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
            containerColor = Color.Transparent, // let the glow show through
            contentColor = MaterialTheme.colorScheme.onSurface,
            bottomBar = {
                // Bottom nav pinned to the bottom
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
                                    if(index==0){
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("homeScreen")
                                    }
                                    if (index == 1) {
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("lostAndFoundScreen")
                                    }
                                    if(index ==2){
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("parkingScreen")
                                    }
                                    if(index==3){
                                        Log.d("tag","Clicking $index")
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.navigate("aboutPage") }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "User Profile",
                                    modifier = Modifier.height(36.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hello ${userName}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "CAMPUS CONNECT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Campus Notice Board",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

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
                                        .width(280.dp)
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
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = { navController.navigate("liveNotesSharing") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("LNS (Live Note Sharing)")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("uploadNotes") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Upload Notes")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("findAllNotes") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Find Notes")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LostAndFoundScreen(navController: NavController, viewmodel: LostViewModel, viewm: FoundViewModel) {

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {

        var search by remember { mutableStateOf("") }
        var search2 by remember { mutableStateOf("") }
        var selectedtabindex by remember { mutableStateOf(0) }
        val tabtitles = listOf("Lost", "Found")
        var footerindex by remember { mutableStateOf(0) }
        var input1 by remember { mutableStateOf("") }
        var input2 by remember { mutableStateOf("") }
        var input3 by remember { mutableStateOf("") }
        var input4 by remember { mutableStateOf("") }
        val footerr: List<Int> = listOf(
            R.drawable.baseline_home_24,
            R.drawable.outline_feature_search_24,
            R.drawable.baseline_local_parking_24,
            R.drawable.outline_person_24
        )

        var foundindex by remember { mutableStateOf(0) }
        val lists by viewmodel.lists.collectAsState()
        val listF by viewm.lists.collectAsState()
        val count by viewmodel.count.collectAsState()
        val count2 by viewm.count2.collectAsState()

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

            if (selectedtabindex == 0) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    label = { Text("Search") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            modifier = Modifier.clickable(true, onClick = {
                                viewmodel.ReadLostByText(search)
                            })
                        )
                    }
                )

                Card(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(Color(0x335B6DFA))
                ) {
                    Column {
                        Text(lists.text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(lists.description, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                OutlinedTextField(value = input1, onValueChange = { input1 = it }, Modifier.padding(start = 65.dp))
                OutlinedTextField(value = input2, onValueChange = { input2 = it }, Modifier.padding(start = 65.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        viewmodel.count.value++
                        viewmodel.WriteLostById(
                            userViewModel = UserViewModel(),
                            count,
                            text = input1,
                            description = input2
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            } else {
                OutlinedTextField(
                    value = search2,
                    onValueChange = { search2 = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    label = { Text("Search") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            modifier = Modifier.clickable(true, onClick = {
                                viewm.ReadFoundByText(search2)
                            })
                        )
                    }
                )

                Card(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(Color(0x335B6DFA))
                ) {
                    Column {
                        Text(listF.text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(listF.description, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                OutlinedTextField(value = input3, onValueChange = { input3 = it }, Modifier.padding(start = 65.dp))
                OutlinedTextField(value = input4, onValueChange = { input4 = it }, Modifier.padding(start = 65.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {

                        // Keeping the same functionality as already added (no new methods)
                        viewm.count2.value++
                        viewm.WriteFoundById(UserViewModel(), viewm.count2.value, input3, input4)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            }

            TabRow(
                selectedTabIndex = footerindex,
                divider = { HorizontalDivider() },
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 130.dp)
                    .fillMaxWidth()
                    .size(100.dp)
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
}

@Composable
fun RadialGlowBackground(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0x335B6DFA),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = glowColor,
                    radius = size.minDimension * 0.6f,
                    center = Offset(x = size.width * 0.25f, y = size.height * 0.15f)
                )
                // Slight right-side glow
                drawCircle(
                    color = glowColor.copy(alpha = 0.18f),
                    radius = size.minDimension * 0.45f,
                    center = Offset(x = size.width * 0.85f, y = size.height * 0.25f)
                )
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
                        label = { Text("Enter PhoneNumber") },
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
        if (password.isEmpty()) {
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
            val failed = rules.filter { (rx, _) -> !rx.containsMatchIn(password) }.map { it.second }
            viewModel.passwordError.value = if (failed.isNotEmpty()) {
                ok = false; "Password must include: ${failed.joinToString(", ")}"
            } else ""
        }

        // Confirm Password
        viewModel.confirmPasswordError.value = when {
            confirmPassword.isEmpty() -> { ok = false; "Please confirm your password" }
            confirmPassword != password -> { ok = false; "Passwords do not match" }
            else -> ""
        }

        return ok
    }

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
                            if (validate()) {
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

    var editConfirmPassword by remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }

    editConfirmPassword = editPassword

    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    var footerindex by remember { mutableStateOf(0) }

    fun validate(): Boolean {
        var ok = true

        // Name
        val nameTrim = editName.trim()
        val nameRegex = Regex("^[A-Za-z][A-Za-z\\s''-]{1,49}$")
        userViewModel.nameError.value = when {
            nameTrim.isEmpty() -> { ok = false; "Name is required" }
            !nameRegex.matches(nameTrim) -> { ok = false; "Use 2–50 letters; spaces, apostrophes, hyphens allowed" }
            else -> ""
        }

        // Phone
        val digitsOnly = editMobileNumber.filter { it.isDigit() }
        val phoneRegex = Regex("^[6-9]\\d{9}$")
        userViewModel.numberError.value = when {
            digitsOnly.isEmpty() -> { ok = false; "Phone number is required" }
            digitsOnly.length != 10 -> { ok = false; "Must be exactly 10 digits" }
            !phoneRegex.matches(digitsOnly) -> { ok = false; "Invalid mobile format (must start with 6-9)" }
            else -> ""
        }

        // Email
        val emailTrim = editEmail.trim()
        userViewModel.emailError.value = when {
            emailTrim.isEmpty() -> { ok = false; "Email is required" }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches() -> {
                ok = false; "Enter a valid email address"
            }
            else -> ""
        }

        // Password
        if (editPassword.isEmpty()) {
            userViewModel.passwordError.value = "Password is required"
            ok = false
        } else {
            val rules = listOf(
                Regex(".{8,}") to "8+ characters",
                Regex("[a-z]") to "one lowercase",
                Regex("[A-Z]") to "one uppercase",
                Regex("\\d") to "one digit",
                Regex("[^A-Za-z0-9]") to "one special character"
            )
            val failed = rules.filter { (rx, _) -> !rx.containsMatchIn(editPassword) }.map { it.second }
            userViewModel.passwordError.value = if (failed.isNotEmpty()) {
                ok = false; "Password must include: ${failed.joinToString(", ")}"
            } else ""
        }

        // Confirm Password
        userViewModel.confirmPasswordError.value = when {
            editConfirmPassword.isEmpty() -> { ok = false; "Please confirm your password" }
            editConfirmPassword != editPassword -> { ok = false; "Passwords do not match" }
            else -> ""
        }

        return ok
    }

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
                                editConfirmPassword = it
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
                                    if (validate()) {
                                        userViewModel.updateUser(
                                            User(
                                                userId,
                                                editName,
                                                editMobileNumber,
                                                editEmail,
                                                editPassword
                                            )
                                        )
                                        Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show()
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
                            if(index==0){
                                Log.d("tag","Clicking $index")
                                navController.navigate("homeScreen")
                            }
                            if (index == 1) {
                                Log.d("tag","Clicking $index")
                                navController.navigate("lostAndFoundScreen")
                            }
                            if(index ==2){
                                Log.d("tag","Clicking $index")
                                navController.navigate("parkingScreen")
                            }
                            if(index==3){
                                Log.d("tag","Clicking $index")
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
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
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
                .fillMaxWidth()
                .padding(top = 30.dp)
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
                .fillMaxWidth()
                .size(100.dp)
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
                        if(index==0){
                            Log.d("tag","Clicking $index")
                            navController.navigate("homeScreen")
                        }
                        if (index == 1) {
                            Log.d("tag","Clicking $index")
                            navController.navigate("lostAndFoundScreen")
                        }
                        if(index ==2){
                            Log.d("tag","Clicking $index")
                            navController.navigate("parkingScreen")
                        }
                        if(index==3){
                            Log.d("tag","Clicking $index")
                            navController.navigate("aboutPage")
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
fun LiveNotesSharing(
    viewModels: LiveNotesSharingViewModel = viewModel(),
    userViewModel: UserViewModel=viewModel(),
    navController: NavController
) {

    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())

    var selectedtabindex by remember { mutableStateOf(0) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )
    var count = 0
    val footerindex by viewModels.footerindex.collectAsState()
    val selectedTab by viewModels.selectedTab.collectAsState()
    val searchQuery by viewModels.searchQuery.collectAsState()
    val searchTitle by viewModels.searchTitle.collectAsState()
    val searchLiveText by viewModels.searchLiveText.collectAsState()
    val goLiveTitle by viewModels.goLiveTitle.collectAsState()
    val goLiveDescription by viewModels.goLiveDescription.collectAsState()
    val liveId by userViewModel.userId.collectAsState()


    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            bottomBar = {
                // Footer pinned to bottom – preserves your original footer logic
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
                                // Your vertical center divider (kept as-is)
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
                                    // PRESERVE: your original behavior
                                    viewModels.footerindex.value = index
                                    if(index==0){
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("homeScreen")
                                    }
                                    if (index == 1) {
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("lostAndFoundScreen")
                                    }
                                    if(index ==2){
                                        Log.d("tag","Clicking $index")
                                        navController.navigate("parkingScreen")
                                    }
                                    if(index==3){
                                        Log.d("tag","Clicking $index")
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
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Live Screen",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                val tabs = listOf("Search", "Go Live")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.70f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { viewModels.selectedTab.value = index },
                            text = {
                                Text(
                                    title,
                                    color = if (selectedTab == index)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ====== SEARCH TAB ======
                if (selectedTab == 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModels.searchQuery.value = it },
                            label = { Text("Search") },
                            placeholder = { Text("Search link...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        )

                        if (count > 0) {
                            viewModels.serachIdinLiveShare(searchQuery)
                        }

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                count++
                                viewModels.serachIdinLiveShare(searchQuery)
                            },
                            modifier = Modifier
                                .padding(start = 140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Text("Search")
                        }

                        Spacer(Modifier.height(12.dp))

                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        ) {
                            Spacer(Modifier.height(5.dp))
                            Text(
                                searchTitle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(5.dp))
                        }

                        Spacer(Modifier.height(12.dp))

                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                searchLiveText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(500.dp)
                                    .padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                } else {
                    // ====== GO LIVE TAB ======
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = goLiveTitle,
                            onValueChange = { newValue -> viewModels.goLiveTitle.value = newValue },
                            label = { Text("Live Title") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = goLiveDescription,
                            onValueChange = {
                                viewModels.goLiveDescription.value = it
                                viewModels.liveNotesSharing(userViewModel, goLiveTitle, goLiveDescription)
                            },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))

                        // PRESERVE: your static text
                        Text(liveId)
                    }
                }

                // Optional bottom spacer so content doesn't touch the bottom bar
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun AppNavigation(startDestination:String){
    val navController=rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
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
            LostAndFoundScreen(navController,viewModel(),viewModel())
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
            LiveNotesSharing(viewModel(), viewModel(),navController)
        }
    }
}
