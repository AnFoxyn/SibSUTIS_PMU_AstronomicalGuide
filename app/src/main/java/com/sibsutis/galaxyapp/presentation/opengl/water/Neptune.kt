package com.sibsutis.galaxyapp.presentation.opengl.water

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sibsutis.galaxyapp.presentation.opengl.moon.PhongMoon

@Composable
fun Neptune(modifier: Modifier = Modifier, context: Context) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2)
                setRenderer(WaterPlanet(context))
            }
        }
    )
}