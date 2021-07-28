package com.example.test.feature.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.test.feature.api.imdb.ImdbApi
import com.example.test.feature.api.jokes.JokesApi
import com.example.test.feature.arch.InterceptorFixer
import com.example.test.feature.logging.Logger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @IsLogTag
    fun provideLogTag() = "TestTag"

    @Provides
    @IsImdbApiKey
    fun provideImdbApiKey() = "k_xyurx9o1"

    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @IsMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IsIoDispatcher
    fun providesIoDispatcher() = Dispatchers.IO

    @Provides
    fun provideGlide(@ApplicationContext context: Context): RequestManager = Glide.with(context)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideJokesApi(json: Json, logger: Logger): JokesApi = prepareRetrofit(json, logger)
        .baseUrl("https://api.icndb.com/")
        .build()
        .create(JokesApi::class.java)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideImdbApi(json: Json, logger: Logger): ImdbApi = prepareRetrofit(json, logger)
        .baseUrl("https://imdb-api.com/")
        .build()
        .create(ImdbApi::class.java)

    private fun prepareRetrofit(json: Json, logger: Logger) = Retrofit.Builder()
        .callFactory(OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(InterceptorFixer(
                HttpLoggingInterceptor { message ->
                    logger.info(message)
                }.also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }
            ))
            .build()
        )
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
}
