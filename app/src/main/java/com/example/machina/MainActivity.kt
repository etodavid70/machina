package com.example.machina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.machina.ui.navigation.AuthNavGraph
import com.example.machina.ui.screens.auth.Pager
import com.example.machina.utils.hasSeenOnboarding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (hasSeenOnboarding(this)) {
                AuthNavGraph()
            } else {
                Pager {
                    // Once onboarding finished
                    recreate() // Reloads and shows main screen next time
                }
            }
        }

    }
}

