package com.testsite.reddittop.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class UiViewModel<State : UiStateHolder, Action : UiStateAction, Event : UiEvent> : ViewModel() {
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
}