package com.sibsutis.galaxyapp.presentation.NewsScreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.common.Resource
import com.sibsutis.galaxyapp.domain.models.News
import com.sibsutis.galaxyapp.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class FourScreenViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(
        FourScreenState(
            false,"",
        )
    )
    val state: State<FourScreenState> = _state

    init {
        getNews()
    }

    private fun getNews(){
        getNewsUseCase().onEach { item ->
            when(item) {
                is Resource.Success -> {
                    _state.value = FourScreenState(
                        news = item.data?.map { news ->
                            News(
                                title = news.title,
                                text = news.text,
                                likes = news.likes
                            )
                        }
                    )
                }
                is Resource.Error -> {
                    _state.value = FourScreenState(
                        error = item.message ?: R.string.item_error.toString()
                    )
                }
                is Resource.Loading -> {
                    _state.value = FourScreenState(
                        error = item.message ?: R.string.item_load.toString()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}