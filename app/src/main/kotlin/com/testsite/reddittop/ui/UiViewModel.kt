package com.testsite.reddittop.ui

import androidx.lifecycle.ViewModel
import com.testsite.reddittop.ui.screen.top.TopPostsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class UiViewModel<T : UiStateHolder, A : UiStateAction, E : UiEvent> : ViewModel() {
    /**
     * UI State to be displayed
     */
    abstract val uiState: StateFlow<UiState<T>>

    /**
     * One-time-events to handle
     */
    abstract val uiEvent: Flow<TopPostsEvent>

    /**
     * Actions from UI to perform
     */
    abstract fun action(action: A)
}