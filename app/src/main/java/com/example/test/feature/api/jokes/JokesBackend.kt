package com.example.test.feature.api.jokes

import com.example.test.feature.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokesBackend @Inject constructor(private val jokesApi: JokesApi, private val logger: Logger) {
    suspend fun jokes(firstName: String, lastName: String) = wrap { jokesApi.jokes(firstName, lastName) }

    private suspend fun wrap(creator: suspend () -> JokesResponse): JokesResponse {
        val response = try {
            creator()
        } catch (e: Exception) {
            logger.error("Network failure", e)
            throw JokesApiException(true)
        }

        if (response.type != JokesResponse.TYPE_SUCCESS) {
            throw JokesApiException(false)
        }

        return response
    }
}
