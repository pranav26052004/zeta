package com.example.projectzeta.ComposableScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R


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