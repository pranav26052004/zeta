package com.example.projectzeta.ComposableScreens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.LiveNotesSharingViewModel
import com.example.projectzeta.ViewModels.UserViewModel


//@Composable
//fun LiveNotesSharing(
//    viewModels: LiveNotesSharingViewModel = viewModel(),
//    userViewModel: UserViewModel=viewModel(),
//    navController: NavController
//) {
//
//    val session = SessionManager(LocalContext.current)
//    userViewModel.getUserandSetState(session.getLoggedInUser())
//
//    var selectedtabindex by remember { mutableStateOf(0) }
//    val footerr: List<Int> = listOf(
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
//    val liveId by userViewModel.userId.collectAsState()
//
//
//    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
//        Scaffold(
//            containerColor = Color.Transparent,
//            contentColor = MaterialTheme.colorScheme.onSurface,
//            bottomBar = {
//                // Footer pinned to bottom – preserves your original footer logic
//                Surface(
//                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
//                    tonalElevation = 2.dp,
//                    shadowElevation = 6.dp,
//                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
//                ) {
//                    TabRow(
//                        selectedTabIndex = selectedtabindex,
//                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        divider = { HorizontalDivider() },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .navigationBarsPadding()
//                            .drawBehind {
//                                // Your vertical center divider (kept as-is)
//                                val x = size.width / 2f
//                                drawLine(
//                                    color = Color.Black,
//                                    start = Offset(x, 0f),
//                                    end = Offset(x, size.height),
//                                    strokeWidth = 2.dp.toPx()
//                                )
//                            }
//                    ) {
//                        footerr.forEachIndexed { index, icon ->
//                            Tab(
//                                selected = footerindex == index,
//                                onClick = {
//                                    viewModels.footerindex.value = index
//                                    if(index==0){
//                                        Log.d("tag","Clicking $index")
//                                        navController.navigate("homeScreen")
//                                    }
//                                    if (index == 1) {
//                                        Log.d("tag","Clicking $index")
//                                        navController.navigate("lostAndFoundScreen")
//                                    }
//                                    if(index ==2){
//                                        Log.d("tag","Clicking $index")
//                                        navController.navigate("parkingScreen")
//                                    }
//                                    if(index==3){
//                                        Log.d("tag","Clicking $index")
//                                        navController.navigate("aboutPage")
//                                    }
//                                },
//                                icon = {
//                                    Icon(
//                                        painterResource(icon),
//                                        contentDescription = null
//                                    )
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//                    .statusBarsPadding()
//                    .padding(horizontal = 20.dp, vertical = 16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Text(
//                    text = "Live Screen",
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp)
//                )
//
//                val tabs = listOf("Search", "Go Live")
//                TabRow(
//                    selectedTabIndex = selectedTab,
//                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.70f),
//                    contentColor = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(14.dp))
//                ) {
//                    tabs.forEachIndexed { index, title ->
//                        Tab(
//                            selected = selectedTab == index,
//                            onClick = { viewModels.selectedTab.value = index },
//                            text = {
//                                Text(
//                                    title,
//                                    color = if (selectedTab == index)
//                                        MaterialTheme.colorScheme.primary
//                                    else
//                                        MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                            }
//                        )
//                    }
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                if (selectedTab == 0) {
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        OutlinedTextField(
//                            value = searchQuery,
//                            onValueChange = { viewModels.searchQuery.value = it },
//                            label = { Text("Search") },
//                            placeholder = { Text("Search link...") },
//                            singleLine = true,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(12.dp))
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//
//                        Button(
//                            onClick = {
//                                viewModels.startLiveObservation(searchQuery)
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(12.dp))
//                        ) {
//                            Text("Search & Join Live")
//                        }
//
//                        Spacer(Modifier.height(12.dp))
//
//                        Card(
//                            elevation = CardDefaults.cardElevation(10.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
//                                contentColor = MaterialTheme.colorScheme.onSurface
//                            ),
//                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
//                        ) {
//                            Spacer(Modifier.height(5.dp))
//                            Text(
//                                searchTitle,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 12.dp, vertical = 8.dp),
//                                style = MaterialTheme.typography.titleMedium,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                            Spacer(Modifier.height(5.dp))
//                        }
//
//                        Spacer(Modifier.height(12.dp))
//
//                        Card(
//                            elevation = CardDefaults.cardElevation(10.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
//                                contentColor = MaterialTheme.colorScheme.onSurface
//                            ),
//                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
//                        ) {
//                            Text(
//                                searchLiveText,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .heightIn(500.dp)
//                                    .padding(12.dp),
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
//                    }
//
//                } else {
//                    // ====== GO LIVE TAB ======
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        OutlinedTextField(
//                            value = goLiveTitle,
//                            onValueChange = { newValue -> viewModels.goLiveTitle.value = newValue },
//                            label = { Text("Live Title") },
//                            singleLine = true,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(12.dp))
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = goLiveDescription,
//                            onValueChange = {
//                                viewModels.goLiveDescription.value = it
//                                viewModels.liveNotesSharing(userViewModel, goLiveTitle, goLiveDescription)
//                            },
//                            label = { Text("Description") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(500.dp)
//                                .clip(RoundedCornerShape(12.dp))
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//
//                        // PRESERVE: your static text
//                        Text("Live Id: "+liveId)
//                    }
//                }
//
//                // Optional bottom spacer so content doesn't touch the bottom bar
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
//}

@Composable
fun LiveNotesSharing(
    viewModels: LiveNotesSharingViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())

    // Define Footer Icons (Matching your other screens)
    val footerr: List<Int> = listOf(
        R.drawable.baseline_home_24,
        R.drawable.outline_feature_search_24,
        R.drawable.baseline_local_parking_24,
        R.drawable.outline_person_24
    )

    // Collect States from ViewModels
    val footerindex by viewModels.footerindex.collectAsState()
    val selectedTab by viewModels.selectedTab.collectAsState()
    val searchQuery by viewModels.searchQuery.collectAsState()
    val searchTitle by viewModels.searchTitle.collectAsState()
    val searchLiveText by viewModels.searchLiveText.collectAsState()
    val goLiveTitle by viewModels.goLiveTitle.collectAsState()
    val goLiveDescription by viewModels.goLiveDescription.collectAsState()
    val liveId by userViewModel.userId.collectAsState()

    RadialGlowBackground(modifier = Modifier.fillMaxSize()) {
        // Main container to replace Scaffold
        Column(modifier = Modifier.fillMaxSize()) {

            // --- 1. CONTENT AREA ---
            Column(
                modifier = Modifier
                    .weight(1f) // Pushes Nav Bar to bottom
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Live Screen",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                // Tab Selector
                val tabs = listOf("Search", "Go Live")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.70f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
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

                if (selectedTab == 0) {
                    // ====== SEARCH TAB ======
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModels.searchQuery.value = it },
                            label = { Text("Search") },
                            placeholder = { Text("Search link...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = { viewModels.startLiveObservation(searchQuery) },
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        ) {
                            Text("Search & Join Live")
                        }

                        Spacer(Modifier.height(12.dp))

                        // Title Card
                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                            )
                        ) {
                            Text(
                                searchTitle,
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Live Text Card
                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                            )
                        ) {
                            Text(
                                searchLiveText,
                                modifier = Modifier.fillMaxWidth().heightIn(500.dp).padding(12.dp),
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
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = goLiveDescription,
                            onValueChange = {
                                viewModels.goLiveDescription.value = it
                                viewModels.liveNotesSharing(userViewModel, goLiveTitle, goLiveDescription)
                            },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth().height(500.dp).clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))
                        Text("Live Id: $liveId", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // --- 2. BOTTOM NAVIGATION BAR ---
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                tonalElevation = 8.dp
            ) {
                footerr.forEachIndexed { index, icon ->
                    NavigationBarItem(
                        selected = footerindex == index,
                        onClick = {
                            viewModels.footerindex.value = index
                            val route = when (index) {
                                0 -> "homeScreen"
                                1 -> "lostAndFoundScreen"
                                2 -> "parkingScreen"
                                else -> "aboutPage"
                            }
                            navController.navigate(route)
                        },
                        icon = { Icon(painterResource(icon), contentDescription = null) },
                        label = {
                            val label = when (index) {
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
