package com.example.test.feature.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ImdbMovie(
    val id: String?, // "tt3554046"
    val rank: String? = null, // "1"
    val rankUpDown: String? = null, // "+1"
    val title: String? = null, // "Space Jam: A New Legacy"
    val fullTitle: String? = null, // "Space Jam: A New Legacy (2021)"
    val year: String? = null, // "2021"
    val image: String? = null, // "https://imdb-api.com/images/original/MV5BZTAzN2ZlZTEtOTA5ZS00MGFjLTliYWMtYTQzYTFlYTIwZDVmXkEyXkFqcGdeQXVyNjY1MTg4Mzc@._V1_Ratio0.6716_AL_.jpg"
    val crew: String? = null, // "Malcolm D. Lee (dir.), LeBron James, Don Cheadle"
    val imDbRating: String? = null, // "4.4"
    val imDbRatingCount: String? = null, // "35602"
) : Parcelable {
    val thumbImage
        get() = image?.replace("https://imdb-api.com/images/original/", "https://imdb-api.com/images/800x400/")
}
