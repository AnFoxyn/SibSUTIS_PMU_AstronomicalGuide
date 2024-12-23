package com.sibsutis.galaxyapp.presentation.opengl.astro

//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sibsutis.galaxyapp.presentation.opengl.moon.Moon
import com.sibsutis.galaxyapp.presentation.opengl.water.Neptune

// TODO Поправить экран

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstroScreen(modifier: Modifier = Modifier, context: Context) {
    var selectedPlanet by remember { mutableIntStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val openGlClass = remember {
        OpenGLAstro(context, selectedPlanet)
    }
    val openGlView = remember {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(openGlClass)
            //renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY //для покадрового рендера
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    text = PlanetsData.name[selectedPlanet]
                )
                Spacer(modifier = Modifier.padding(8.dp))
                if (selectedPlanet == 3) Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) { Moon(context = context) }
                else if(selectedPlanet == PlanetsData.name.size - 1)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) { Neptune(context = context) }
                else {
                    Image(
                        ImageBitmap.imageResource(PlanetsData.planetImage[selectedPlanet]), ""
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedCard(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = PlanetsData.description[selectedPlanet]
                    )
                }
            }
        }
    }

    AndroidView(
        factory = {
            openGlView
        },
        update = {
            openGlClass.setSelectedPlanet(selectedPlanet)
            openGlView.requestRender()
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = {
                    if (selectedPlanet > 0) selectedPlanet--
                    else selectedPlanet = 0
                    openGlClass.setSelectedPlanet(selectedPlanet)
                },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "") }

            Button(
                modifier = Modifier.size(width = 200.dp, height = 41.dp),
                onClick = {
                showBottomSheet = true
            }) {
                Text(text = PlanetsData.name[selectedPlanet])
            }

            IconButton(
                onClick = {
                    if (selectedPlanet < PlanetsData.name.size - 1) selectedPlanet++
                    else selectedPlanet = PlanetsData.name.size - 1
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

    /*Column {
        Box(
            modifier = Modifier
                .weight(0.6f)
                .size(400.dp)
        ) {
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
                    if (selectedPlanet > 0) selectedPlanet--
                    else selectedPlanet = 0
                    openGlClass.setSelectedPlanet(selectedPlanet)
                },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "") }

            Button(onClick = {
                showBottomSheet = true
            }) {
                Text(text = PlanetsData.name[selectedPlanet])
            }

            IconButton(
                onClick = {
                    if (selectedPlanet < PlanetsData.name.size - 1) selectedPlanet++
                    else selectedPlanet = PlanetsData.name.size - 1
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
    }*/
}