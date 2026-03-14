package com.example.projectzeta.ComposableScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.myapplication.R
import com.example.myapplication.RadialGlowBackground
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ViewModels.FoundViewModel
import com.example.projectzeta.ViewModels.LostViewModel
import com.example.projectzeta.ViewModels.UserViewModel

@Composable
fun LostAndFoundScreen(navController: NavController, viewmodel: LostViewModel, viewm: FoundViewModel) {

    val userViewModel: UserViewModel = viewModel()
    val session = SessionManager(LocalContext.current)
    userViewModel.getUserandSetState(session.getLoggedInUser())

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
        val count by userViewModel.userId.collectAsState()
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
                        Text("Lost Item: "+lists.text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Details: "+lists.description, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Lost By: "+lists.lostByUser, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                OutlinedTextField(value = input1, onValueChange = { input1 = it }, Modifier.padding(start = 65.dp), label = {Text("Enter Lost Item")})
                OutlinedTextField(value = input2, onValueChange = { input2 = it }, Modifier.padding(start = 65.dp), label = {Text("Describe it")})

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
                            userViewModel = userViewModel,
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
                        Text("Found Item: "+lists.text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Details: "+lists.description, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Found By: "+lists.lostByUser, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                OutlinedTextField(value = input3, onValueChange = { input3 = it }, Modifier.padding(start = 65.dp), label = {Text("Enter Found item")})
                OutlinedTextField(value = input4, onValueChange = { input4 = it }, Modifier.padding(start = 65.dp), label = {Text("Describe it")})

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        viewm.count2.value++
                        viewm.WriteFoundById(UserViewModel(), input3, input4)

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
