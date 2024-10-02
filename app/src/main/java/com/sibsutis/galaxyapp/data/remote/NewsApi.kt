package com.sibsutis.galaxyapp.data.remote

import com.sibsutis.galaxyapp.data.remote.dto.NewsDto

interface NewsApi {
    suspend fun GetNews() : List<NewsDto>
}