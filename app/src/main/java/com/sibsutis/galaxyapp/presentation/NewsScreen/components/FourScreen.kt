package com.sibsutis.galaxyapp.presentation.NewsScreen.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sibsutis.galaxyapp.domain.models.News
import com.sibsutis.galaxyapp.presentation.NewsScreen.FourScreenViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FourScreen(
    viewModel: FourScreenViewModel = hiltViewModel()
){
    val state = viewModel.state.value

    LazyVerticalGrid(
        modifier = Modifier.padding(),
        columns = GridCells.Fixed(2)
    ){
        items(state.news?.size ?: 0){ index ->
            NewsCard(state.news?.get(index) ?: News("","",0))
        }

    }
}