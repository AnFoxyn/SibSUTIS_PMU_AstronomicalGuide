package com.sibsutis.galaxyapp.domain.usecase

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.common.Resource
import com.sibsutis.galaxyapp.data.converter.toNews
import com.sibsutis.galaxyapp.domain.models.News
import com.sibsutis.galaxyapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    // подготовка под получение данных с инета
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7) // хз
    operator fun invoke(): Flow<Resource<List<News>>> = flow {
        try {
            emit(Resource.Loading())
            val news = repository.getNews()

            emit(Resource.Success<List<News>>(toNews(news)))// криво капец
        } catch (e: HttpException){
        emit(Resource.Error<List<News>>(e.localizedMessage ?: R.string.http_error.toString()))
    } catch (e: IOException){
        emit(Resource.Error<List<News>>(e.localizedMessage ?: R.string.io_error.toString()))
    }

    }
}