package com.sibsutis.galaxyapp.presentation.opengl.cube

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CubeScreen(modifier: Modifier = Modifier, context: Context){
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2)
                setRenderer(OpenGLCube(context))
            }
        }
    )
}