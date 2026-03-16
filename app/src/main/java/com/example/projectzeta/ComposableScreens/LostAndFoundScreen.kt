package com.example.projectzeta.ComposableScreens

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.FoundViewModel
import com.example.projectzeta.ViewModels.LostViewModel
import com.example.projectzeta.ViewModels.UserViewModel
import com.example.projectzeta.model.Found
import com.example.projectzeta.model.Lost

@Composable
fun LostAndFoundScreen(navController: NavController, viewmodel: LostViewModel, viewm: FoundViewModel) {
    val userViewModel: UserViewModel = viewModel()
    val session = SessionManager(LocalContext.current)

    LaunchedEffect(Unit) {
        userViewModel.getUserandSetState(session.getLoggedInUser())
    }

    var search by remember { mutableStateOf("") }
    var search2 by remember { mutableStateOf("") }
    var selectedtabindex by remember { mutableStateOf(0) }
    val tabtitles = listOf("Lost", "Found")

    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var input3 by remember { mutableStateOf("") }
    var input4 by remember { mutableStateOf("") }

    val lists by viewmodel.lists.collectAsState()
    val listF by viewm.lists.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        Triple("Home", R.drawable.baseline_home_24, "homeScreen"),
                        Triple("Lost & Found", R.drawable.outline_feature_search_24, "lostAndFoundScreen"),
                        Triple("Parking", R.drawable.baseline_local_parking_24, "parkingScreen"),
                        Triple("Profile", R.drawable.outline_person_24, "aboutPage")
                    )

                    navItems.forEach { (label, icon, route) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                if (currentRoute != route) {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(painterResource(id = icon), contentDescription = label) },
                            label = { Text(label, fontSize = 12.sp) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
            ) {
                TabRow(
                    selectedTabIndex = selectedtabindex,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = { HorizontalDivider() }
                ) {
                    tabtitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedtabindex == index,
                            onClick = { selectedtabindex = index },
                            text = { Text(text = title, fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    if (selectedtabindex == 0) {
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Search Lost Items") },
                            trailingIcon = {
                                IconButton(onClick = { viewmodel.ReadLostByText(search) }) {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            }
                        )

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            LazyColumn(modifier = Modifier.padding(12.dp)) {
                                items(lists.size) { index ->
                                    Column {
                                        Text("Lost item: ${lists[index].text}", fontWeight = FontWeight.Bold,fontSize = 15.sp)
                                        Text("Details: ${lists[index].description}",fontSize = 15.sp,fontWeight = FontWeight.Bold)
                                        Text("Lost By: ${lists[index].lostByUser}",fontSize = 15.sp,fontWeight = FontWeight.Bold)
                                        Text("................",fontWeight = FontWeight.Bold)
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                                    }
                                }
                            }
                        }
                        OutlinedTextField(value = input1, onValueChange = { input1 = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Item Name") })
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = input2, onValueChange = { input2 = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Description") })

                        IconButton(
                            onClick = {
                                viewmodel.count.value++
                                viewmodel.WriteLostById(userViewModel, input1, input2)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Lost", tint = MaterialTheme.colorScheme.primary)
                        }

                    } else {
                        OutlinedTextField(
                            value = search2,
                            onValueChange = { search2 = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Search Found Items") },
                            trailingIcon = {
                                IconButton(onClick = { viewm.ReadFoundByText(search2) }) {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            }
                        )

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            LazyColumn(modifier = Modifier.padding(12.dp)) {
                                items(listF.size) { index ->
                                    Column {
                                        Text("Found item: ${listF[index].text}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Text("Details: ${listF[index].description}", fontSize = 15.sp,fontWeight = FontWeight.Bold)
                                        Text("Found By: ${listF[index].foundByUser}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("......................", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        OutlinedTextField(value = input3, onValueChange = { input3 = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Item Name") })
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = input4, onValueChange = { input4 = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Description") })

                        IconButton(
                            onClick = {
                                viewm.count2.value++
                                viewm.WriteFoundById(userViewModel, input3, input4)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Found", tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
