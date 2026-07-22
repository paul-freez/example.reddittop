package com.testsite.reddittop.ui

interface UiStateHolder
sealed interface UiState<out T: UiStateHolder> {
    data object Loading : UiState<Nothing>
    data class Available<out T: UiStateHolder>(val data: T) : UiState<T>
    data class Error(val error: Throwable) : UiState<Nothing>
}
