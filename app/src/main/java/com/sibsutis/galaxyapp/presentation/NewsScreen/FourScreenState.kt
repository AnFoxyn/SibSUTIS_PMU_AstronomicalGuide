package com.sibsutis.galaxyapp.presentation.NewsScreen

import com.sibsutis.galaxyapp.domain.models.News

data class FourScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    var news: List<News>? = emptyList()
)
