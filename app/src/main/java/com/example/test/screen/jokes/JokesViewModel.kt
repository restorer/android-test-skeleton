package com.example.test.screen.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.feature.api.jokes.JokesApiException
import com.example.test.feature.api.jokes.JokesBackend
import com.example.test.feature.di.IsIoDispatcher
import com.example.test.feature.entities.JokesJoke
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokesViewModel @AssistedInject constructor(
    private val jokesBackend: JokesBackend,
    @IsIoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @Assisted("networkErrorMessage") private val networkErrorMessage: String,
    @Assisted("apiErrorMessage") private val apiErrorMessage: String
) : ViewModel() {
    companion object {
        private const val FIRST_NAME = "John"
        private const val LAST_NAME = "Doe"
    }

    private val _isLoading = MutableLiveData(true)
    private val _errorMessage = MutableLiveData<String?>()
    private val _jokes = MutableLiveData<List<JokesJoke>?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    val jokes: LiveData<List<JokesJoke>?>
        get() = _jokes

    init {
        refresh()
    }

    fun refresh() {
        moveToLoading()

        viewModelScope.launch {
            try {
                val response = withContext(ioDispatcher) {
                    jokesBackend.jokes(FIRST_NAME, LAST_NAME)
                }

                moveToResult(response.value)
            } catch (e: JokesApiException) {
                moveToError(if (e.isNetworkError) networkErrorMessage else apiErrorMessage)
            }
        }
    }

    private fun moveToLoading() {
        _isLoading.value = true
        _errorMessage.value = null
    }

    private fun moveToResult(result: List<JokesJoke>) {
        _isLoading.value = false
        _errorMessage.value = null
        _jokes.value = result
    }

    private fun moveToError(message: String) {
        _isLoading.value = false
        _errorMessage.value = message
        _jokes.value = null
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("networkErrorMessage") networkErrorMessage: String,
            @Assisted("apiErrorMessage") apiErrorMessage: String
        ): JokesViewModel
    }
}
