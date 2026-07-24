package com.testsite.reddittop.ui

import androidx.lifecycle.viewModelScope
import com.testsite.reddittop.components.managers.AppEventsManager
import com.testsite.reddittop.ui.app.AppEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class UIHostedViewModel<State : UiStateHolder, Action : UiStateAction, Event : UiEvent> constructor(
    eventsManager: AppEventsManager
) : UiViewModel<State, Action, Event>() {
    init {
        eventsManager.appEvent
            .onEach {
                when (it) {
                    AppEvent.RefreshPage -> loadContent()
                }
            }
            .launchIn(viewModelScope)
    }

    protected abstract fun loadContent()
}