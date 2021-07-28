package com.example.test.feature.api.jokes

import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    @GET("jokes")
    suspend fun jokes(
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String
    ): JokesResponse
}
