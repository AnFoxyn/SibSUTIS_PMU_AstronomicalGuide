package com.sibsutis.galaxyapp.presentation.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AstroScreen(modifier: Modifier = Modifier, context: Context) {
    var selectedPlanet by remember { mutableIntStateOf(0) }

    val openGlClass = remember {
        OpenGLAstro(context, selectedPlanet)
    }

    val openGlView = remember {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(openGlClass)
            //renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        }
    }

    Column {
        Box(
            modifier = Modifier
                .weight(0.6f)
                .size(400.dp)
        ){
            AndroidView(
                factory = {
                    openGlView
                },
                update = {
                    openGlClass.setSelectedPlanet(selectedPlanet)
                    openGlView.requestRender()
                }
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = {
                    selectedPlanet--
                    openGlClass.setSelectedPlanet(selectedPlanet)
                },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "") }

            Button(onClick = {}) { }

            IconButton(
                onClick = {
                    selectedPlanet++
                    openGlClass.setSelectedPlanet(selectedPlanet)
                },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = ""
                )
            }
        }
    }
}