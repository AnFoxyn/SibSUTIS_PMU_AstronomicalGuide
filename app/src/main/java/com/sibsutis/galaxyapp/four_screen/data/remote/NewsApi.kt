package com.sibsutis.galaxyapp.four_screen.data.remote

import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto

interface NewsApi {
    suspend fun GetNews() : List<NewsDto>
}