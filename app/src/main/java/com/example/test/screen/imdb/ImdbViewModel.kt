package com.example.test.screen.imdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.feature.api.imdb.ImdbApiException
import com.example.test.feature.api.imdb.ImdbBackend
import com.example.test.feature.di.IsIoDispatcher
import com.example.test.feature.entities.ImdbMovie
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImdbViewModel @AssistedInject constructor(
    private val imdbBackend: ImdbBackend,
    @IsIoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @Assisted("networkErrorMessage") private val networkErrorMessage: String,
    @Assisted("apiErrorMessageFormat") private val apiErrorMessageFormat: String
) : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    private val _errorMessage = MutableLiveData<String?>()
    private val _movies = MutableLiveData<List<ImdbMovie>?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    val movies: LiveData<List<ImdbMovie>?>
        get() = _movies

    init {
        refresh()
    }

    fun refresh() {
        moveToLoading()

        viewModelScope.launch {
            try {
                val response = withContext(ioDispatcher) {
                    imdbBackend.top250Tvs()
                }

                moveToResult(response)
            } catch (e: ImdbApiException) {
                moveToError(
                    if (e.isNetworkError) {
                        networkErrorMessage
                    } else {
                        String.format(apiErrorMessageFormat, e.errorMessage)
                    }
                )
            }
        }
    }

    private fun moveToLoading() {
        _isLoading.value = true
        _errorMessage.value = null
    }

    private fun moveToResult(result: List<ImdbMovie>) {
        _isLoading.value = false
        _errorMessage.value = null
        _movies.value = result
    }

    private fun moveToError(message: String) {
        _isLoading.value = false
        _errorMessage.value = message
        _movies.value = null
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("networkErrorMessage") networkErrorMessage: String,
            @Assisted("apiErrorMessageFormat") apiErrorMessageFormat: String
        ): ImdbViewModel
    }
}
