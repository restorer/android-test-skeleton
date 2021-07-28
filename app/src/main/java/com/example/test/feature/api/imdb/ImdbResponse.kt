package com.example.test.feature.api.imdb

import kotlinx.serialization.Serializable

@Serializable
data class ImdbResponse<T>(val errorMessage: String?, val items: List<T>)
