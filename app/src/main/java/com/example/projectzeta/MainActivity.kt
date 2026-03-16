package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.projectzeta.ComposableScreens.AboutPage
import com.example.projectzeta.ComposableScreens.FindAllNotes
import com.example.projectzeta.ComposableScreens.HomeScreen
import com.example.projectzeta.ComposableScreens.LiveNotesSharing
import com.example.projectzeta.ComposableScreens.LoginScreen
import com.example.projectzeta.ComposableScreens.LostAndFoundScreen
import com.example.projectzeta.ComposableScreens.ParkingScreen
import com.example.projectzeta.SessionManager
import com.example.projectzeta.ComposableScreens.SignUpScreen
import com.example.projectzeta.ComposableScreens.UploadNotes

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
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
fun AppNavigation(startDestination:String){
    val navController=rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(viewModel(),navController)
        }
        composable("signupScreen") {
            SignUpScreen(viewModel(),navController)
        }
        composable("homeScreen") {
            HomeScreen(viewModel(),navController)
        }
        composable("parkingScreen") {
            ParkingScreen(viewModel(),navController)
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
