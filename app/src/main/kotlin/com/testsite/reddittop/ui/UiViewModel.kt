package com.testsite.reddittop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class UiViewModel<State : UiStateHolder, Action : UiStateAction, Event : UiEvent> : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    protected val baseViewModelScope: CoroutineScope = viewModelScope + exceptionHandler

    /**
     * UI State to be displayed
     */
    abstract val uiState: StateFlow<UiState<State>>

    /**
     * One-time-events to handle
     */
    abstract val uiEvent: Flow<Event>

    /**
     * Actions from UI to perform
     */
    abstract fun action(action: Action)

    protected open fun handleException(throwable: Throwable) {
        Timber.e(throwable)
    }
}