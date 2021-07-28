package com.example.test.feature.entities

import kotlinx.serialization.Serializable

@Serializable
data class JokesJoke(val id: Long, val joke: String, val categories: List<String>)
