package com.sibsutis.galaxyapp.di

import com.sibsutis.galaxyapp.data.remote.NewsApi
import com.sibsutis.galaxyapp.data.remote.NewsApiImpl
import com.sibsutis.galaxyapp.data.repository.NewsRepositoryImpl
import com.sibsutis.galaxyapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return NewsApiImpl()
    }

    @Provides
    @Singleton
    fun providesNewsRepository(api: NewsApi): NewsRepository {
        return NewsRepositoryImpl(api)
    }

}