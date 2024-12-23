package com.sibsutis.galaxyapp.presentation.NewsScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sibsutis.galaxyapp.domain.models.News

@Composable
fun NewsCard(news: News){
    //var totallikes = news.likes

    var totallikes by remember { mutableStateOf(news.likes) }

    OutlinedCard(
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = news.title,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = news.text,
            fontSize = 13.sp
        )
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = {totallikes++}) {
                Text("❤ \uFE0E ${totallikes}")
            }
        }
    }
}