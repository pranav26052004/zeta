package com.example.projectzeta.ComposableScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R

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