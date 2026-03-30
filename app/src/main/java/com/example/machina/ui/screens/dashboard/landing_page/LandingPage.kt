package com.example.machina.ui.screens.dashboard.landing_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.machina.ui.navigation.NavigationGraph
import com.example.machina.ui.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LandingPage() {
    val isDarkTheme = rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()


    MaterialTheme(
        colorScheme = if (isDarkTheme.value) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            },


        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NavigationGraph(navController)

            }


        }
    }
}

