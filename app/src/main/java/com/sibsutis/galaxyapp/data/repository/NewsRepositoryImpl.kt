package com.sibsutis.galaxyapp.data.repository

import com.sibsutis.galaxyapp.data.remote.NewsApi
import com.sibsutis.galaxyapp.data.remote.dto.NewsDto
import com.sibsutis.galaxyapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : NewsRepository {

    override suspend fun getNews(): List<NewsDto> {
        return api.GetNews()
    }
}