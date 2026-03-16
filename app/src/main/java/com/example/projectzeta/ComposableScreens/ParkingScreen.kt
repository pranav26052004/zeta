package com.example.projectzeta.ComposableScreens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.ReservationViewModel
import com.example.projectzeta.ViewModels.UserViewModel


//@Composable
//fun ParkingScreen(viewModel: ReservationViewModel = viewModel(), navController: NavController) {
//
//    val slots by viewModel.slots.collectAsState()
//    val userViewModel: UserViewModel = viewModel()
//    val session = SessionManager(LocalContext.current)
//    userViewModel.getUserandSetState(session.getLoggedInUser())
//
//    var footerindex by remember { mutableStateOf(0) }
//    val footerr: List<Int> = listOf(
//        R.drawable.baseline_home_24,
//        R.drawable.outline_feature_search_24,
//        R.drawable.baseline_local_parking_24,
//        R.drawable.outline_person_24
//    )
//    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
//        Column(
//            Modifier
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column {
//                Text("Parking Area", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//            Column(Modifier.weight(1f)) {
//                LazyVerticalGrid(columns = GridCells.Fixed(4)) {
//                    items(slots, key = { it.parkingId }) { slot ->
//                        var color = if (slot.available) Color.Green else Color.Red
//                        Column(
//                            Modifier
//                                .padding(8.dp)
//                                .height(65.dp)
//                                .background(color)
//                                .clickable {
//                                    viewModel.reserveWithDb(slot, userViewModel.nameOfUser.value)
//                                    if (slot.available) {
//                                        color = Color.Green
//                                    } else {
//                                        color = Color.Red
//                                    }
//                                },
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Spacer(Modifier.height(2.dp))
//                            Icon(
//                                painterResource(R.drawable.parking_icon),
//                                contentDescription = "ParkingIcon",
//                                Modifier.size(35.dp),
//                                tint = Color.Magenta
//                            )
//                            Spacer(Modifier.height(2.dp))
//                            Text("Slot: ${slot.parkingId}", color = Color.White)
//                        }
//                    }
//                }
//            }
//            Spacer(Modifier.height(18.dp))
//            Column(
//                Modifier
//                    .height(250.dp)
//                    .padding(8.dp, 0.dp)
//            ) {
//                Text("Total Slots: ${slots.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
//                    Card(
//                        elevation = CardDefaults.cardElevation(10.dp),
//                        modifier = Modifier.padding(5.dp)
//                    ) {
//                        Column {
//                            Text(
//                                "Available Slots: ",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 18.sp
//                            )
//                            LazyColumn {
//                                items(slots.size) { it ->
//                                    if (slots[it].available)
//                                        Text(slots[it].parkingId.toString())
//                                }
//                            }
//                        }
//                    }
//                    Spacer(Modifier.width(50.dp))
//                    Column {
//                        Card(
//                            elevation = CardDefaults.cardElevation(20.dp),
//                            modifier = Modifier.padding(5.dp)
//                        ) {
//                            Text("Reserved Slots: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                            LazyColumn {
//                                items(slots.size) { it ->
//                                    if (!slots[it].available)
//                                        Text(slots[it].parkingId.toString())
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            Spacer(Modifier.height(18.dp))
//            Column() {
//                TabRow(
//                    selectedTabIndex = footerindex,
//                    divider = { HorizontalDivider() },
//                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    modifier = Modifier
//                        .padding(top = 130.dp)
//                        .fillMaxWidth()
//                        .size(100.dp)
//                        .drawBehind {
//                            val x = size.width / 2f
//                            drawLine(
//                                color = Color.Black,
//                                start = Offset(x, 0f),
//                                end = Offset(x, size.height),
//                                strokeWidth = 2.dp.toPx()
//                            )
//                        }
//                ) {
//                    footerr.forEachIndexed { index, icon ->
//                        Tab(
//                            selected = footerindex == index,
//                            onClick = {
//                                footerindex = index
//                                if (index == 0) {
//                                    Log.d("tag", "Clicking $index")
//                                    navController.navigate("homeScreen")
//                                }
//                                if (index == 1) {
//                                    Log.d("tag", "Clicking $index")
//                                    navController.navigate("lostAndFoundScreen")
//                                }
//                                if (index == 2) {
//                                    Log.d("tag", "Clicking $index")
//                                    navController.navigate("parkingScreen")
//                                }
//                                if (index == 3) {
//                                    Log.d("tag", "Clicking $index")
//                                    navController.navigate("aboutPage")
//                                }
//                            },
//                            icon = {
//                                Image(
//                                    painterResource(icon),
//                                    contentDescription = null
//                                )
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun ParkingScreen(viewModel: ReservationViewModel = viewModel(), navController: NavController) {

    val slots by viewModel.slots.collectAsState()
    val userViewModel: UserViewModel = viewModel()
    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())

    var footerindex by remember { mutableStateOf(2) }
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        // Main container to hold content and bottom bar
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. CONTENT AREA (Takes up all space except the bottom bar)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp)) // Padding for status bar
                Text("Parking Area", fontWeight = FontWeight.Bold, fontSize = 24.sp)

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
                            Column(Modifier.padding(8.dp)) {
                                Text("Available Slots: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                LazyColumn {
                                    items(slots.size) { it ->
                                        if (slots[it].available)
                                            Text(slots[it].parkingId.toString())
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.width(50.dp))
                        Card(
                            elevation = CardDefaults.cardElevation(20.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Column(Modifier.padding(8.dp)) {
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
            }

            // 2. NAVIGATION BAR (Stays at the bottom)
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
                            Log.d("tag", "Clicking $index")
                            navController.navigate(route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null
                            )
                        },
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

