package com.example.test.feature.api.jokes

import com.example.test.feature.entities.JokesJoke
import kotlinx.serialization.Serializable

@Serializable
data class JokesResponse(val type: String, val value: List<JokesJoke>) {
    companion object {
        const val TYPE_SUCCESS = "success"
    }
}
