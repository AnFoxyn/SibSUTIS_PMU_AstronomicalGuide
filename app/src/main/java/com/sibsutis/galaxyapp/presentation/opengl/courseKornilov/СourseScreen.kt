package com.sibsutis.galaxyapp.presentation.opengl.courseKornilov

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CourseScreen(modifier: Modifier = Modifier, context: Context){
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2)
                setRenderer(TableRender(context))
            }
        }
    )
}