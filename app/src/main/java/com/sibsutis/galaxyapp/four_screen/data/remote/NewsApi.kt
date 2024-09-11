package com.sibsutis.galaxyapp.four_screen.data.remote

import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto
import com.sibsutis.galaxyapp.four_screen.domain.models.News

interface NewsApi {
    suspend fun GetNews() : List<NewsDto>
}