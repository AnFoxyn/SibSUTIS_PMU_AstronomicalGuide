package com.sibsutis.galaxyapp.presentation.AdvScreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.common.Resource
import com.sibsutis.galaxyapp.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.random.Random

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class AdvViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(
        AdvState(
            false,"",
        )
    )
    val state: State<AdvState> = _state

    init {
        getNews()
    }

    private fun getNews(){
        getNewsUseCase().onEach { item ->
            when(item) {
                is Resource.Success -> {
                    _state.value = AdvState(
                        news = item.data?.get(Random.nextInt(0,9))
                    )
                }
                is Resource.Error -> {
                    _state.value = AdvState(
                        error = item.message ?: R.string.item_error.toString()
                    )
                }
                is Resource.Loading -> {
                    _state.value = AdvState(
                        error = item.message ?: R.string.item_load.toString()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}