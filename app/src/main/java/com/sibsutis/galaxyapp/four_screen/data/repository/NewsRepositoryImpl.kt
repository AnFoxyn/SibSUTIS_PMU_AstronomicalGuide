package com.sibsutis.galaxyapp.four_screen.data.repository

import com.sibsutis.galaxyapp.four_screen.data.remote.NewsApi
import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto
import com.sibsutis.galaxyapp.four_screen.data.remote.localdata.PremadeNews
import com.sibsutis.galaxyapp.four_screen.domain.models.News
import com.sibsutis.galaxyapp.four_screen.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : NewsRepository {

    override suspend fun getNews(): List<NewsDto> {
        return api.GetNews()
    }
}