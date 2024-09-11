package com.sibsutis.galaxyapp.four_screen.presentation

import com.sibsutis.galaxyapp.four_screen.domain.models.News

data class FourScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    var news: List<News>? = emptyList()
)
