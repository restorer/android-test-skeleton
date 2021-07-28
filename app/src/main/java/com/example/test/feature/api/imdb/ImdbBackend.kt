package com.example.test.feature.api.imdb

import com.example.test.feature.di.IsImdbApiKey
import com.example.test.feature.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImdbBackend @Inject constructor(
    private val imdbApi: ImdbApi,
    @IsImdbApiKey private val apiKey: String,
    private val logger: Logger
) {
    suspend fun top250Tvs() = wrap { imdbApi.top250Tvs(apiKey) }
    suspend fun mostPopularMovies() = wrap { imdbApi.mostPopularMovies(apiKey) }

    private suspend fun <T> wrap(creator: suspend () -> ImdbResponse<T>): List<T> {
        val response = try {
            creator()
        } catch (e: Exception) {
            logger.error("Network failure", e)
            throw ImdbApiException(null)
        }

        if (response.errorMessage?.isNotEmpty() == true) {
            throw ImdbApiException(response.errorMessage)
        }

        return response.items
    }
}
