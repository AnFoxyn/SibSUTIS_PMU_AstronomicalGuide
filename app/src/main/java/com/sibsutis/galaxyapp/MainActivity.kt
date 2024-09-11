package com.sibsutis.galaxyapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sibsutis.galaxyapp.four_screen.presentation.components.FourScreen
import com.sibsutis.galaxyapp.ui.theme.GalaxyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            GalaxyAppTheme {
                //val navController = rememberNavController()
                //val basketViewModel : BasketViewModel = hiltViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    ){
                        FourScreen()
                    }
                }
            }
        }
    }
}