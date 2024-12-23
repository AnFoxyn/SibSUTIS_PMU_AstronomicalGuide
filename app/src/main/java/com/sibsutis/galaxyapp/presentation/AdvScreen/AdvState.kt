package com.sibsutis.galaxyapp.presentation.AdvScreen

import com.sibsutis.galaxyapp.domain.models.News

data class AdvState(
    val isLoading: Boolean = false,
    val error: String = "",
    var news: News? = null
)
