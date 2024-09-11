package com.sibsutis.galaxyapp.four_screen.data.converter

import com.sibsutis.galaxyapp.four_screen.data.remote.dto.NewsDto
import com.sibsutis.galaxyapp.four_screen.domain.models.News
import kotlin.random.Random

fun toNews(news : List<NewsDto>) : List<News>{
    return news.map { newsDto ->
        News(
            title = newsDto.title,
            text = newsDto.text,
            likes = Random.nextLong(9999, 1000000000)
        )
    }
}