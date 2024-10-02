package com.sibsutis.galaxyapp.domain.repository

import com.sibsutis.galaxyapp.data.remote.dto.NewsDto

interface NewsRepository {
    suspend fun getNews() : List<NewsDto>
}