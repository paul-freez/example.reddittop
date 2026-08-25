package com.testsite.reddittop.ui

import com.testsite.reddittop.components.managers.AppEventsManager
import com.testsite.reddittop.ui.app.AppEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

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
            .launchIn(baseViewModelScope)

        baseViewModelScope.launch {
            yield() // Make sure that everything is initialized
            loadContent()
        }
    }

    protected abstract fun loadContent()
}