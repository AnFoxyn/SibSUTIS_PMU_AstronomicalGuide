package com.sibsutis.galaxyapp.four_screen.domain.usecase

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.common.Resource
import com.sibsutis.galaxyapp.four_screen.data.converter.toNews
import com.sibsutis.galaxyapp.four_screen.domain.models.News
import com.sibsutis.galaxyapp.four_screen.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import kotlin.math.truncate

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