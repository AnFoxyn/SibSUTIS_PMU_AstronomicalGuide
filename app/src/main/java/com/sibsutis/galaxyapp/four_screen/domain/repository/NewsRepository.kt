package com.sibsutis.galaxyapp.four_screen.domain.repository

import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto
import com.sibsutis.galaxyapp.four_screen.domain.models.News

interface NewsRepository {
    suspend fun getNews() : List<NewsDto>
}