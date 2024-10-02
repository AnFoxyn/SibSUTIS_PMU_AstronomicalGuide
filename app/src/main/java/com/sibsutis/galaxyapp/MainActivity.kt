package com.sibsutis.galaxyapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import com.sibsutis.galaxyapp.presentation.MainScreen
import com.sibsutis.galaxyapp.presentation.theme.GalaxyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            GalaxyAppTheme {
                MainScreen()
            }
        }
    }
}