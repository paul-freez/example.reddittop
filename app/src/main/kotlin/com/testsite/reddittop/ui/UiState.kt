package com.testsite.reddittop.ui

/**
 * Mapper interface for UI states for screens
 */
interface UiStateHolder
sealed interface UiState<out T: UiStateHolder> {
    data object Loading : UiState<Nothing>
    data class Available<out T: UiStateHolder>(val data: T) : UiState<T>
    data class Error(val error: Throwable) : UiState<Nothing>
}
