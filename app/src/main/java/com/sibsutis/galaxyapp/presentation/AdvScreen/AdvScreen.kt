package com.sibsutis.galaxyapp.presentation.AdvScreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sibsutis.galaxyapp.presentation.NewsScreen.FourScreenViewModel
import com.sibsutis.galaxyapp.presentation.NewsScreen.components.NewsCard

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun advScreen(
    viewModel: AdvViewModel = hiltViewModel(),
    onExitClick: () -> Unit
) {
    val state = viewModel.state.value
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {onExitClick()},
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        ) {
            //state?.news?.let { NewsCard(news = it) }
            Card(
                onClick = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colors = CardDefaults.cardColors()
            ) {
                state?.news?.let {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(
                                it.title.uppercase(),
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                it.text,
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                            /*Text(
                                "Просмотров: "
                                        +it.likes.toString(),
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )*/
                        }


                    }
                }
            }
        }
    }
}