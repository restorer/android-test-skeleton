package com.example.test.feature.api.imdb

class ImdbApiException(val errorMessage: String?) : Exception() {
    val isNetworkError = (errorMessage == null)
}
