package com.example.test.feature.api.imdb

import com.example.test.feature.entities.ImdbMovie
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApi {
    @GET("/en/API/Top250TVs/{apiKey}")
    suspend fun top250Tvs(@Path("apiKey") apiKey: String): ImdbResponse<ImdbMovie>

    @GET("/en/API/MostPopularMovies/{apiKey}")
    suspend fun mostPopularMovies(@Path("apiKey") apiKey: String): ImdbResponse<ImdbMovie>
}
