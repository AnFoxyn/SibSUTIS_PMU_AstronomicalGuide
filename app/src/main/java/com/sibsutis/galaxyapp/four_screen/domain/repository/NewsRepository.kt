package com.sibsutis.galaxyapp.four_screen.domain.repository

import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto

interface NewsRepository {
    suspend fun getNews() : List<NewsDto>
}