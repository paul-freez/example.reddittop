package com.testsite.reddittop.ui.app

import androidx.lifecycle.viewModelScope
import com.testsite.reddittop.components.managers.AppEventsManager
import com.testsite.reddittop.data.client.repo.ClientRepository
import com.testsite.reddittop.ui.UiEvent
import com.testsite.reddittop.ui.UiState
import com.testsite.reddittop.ui.UiStateAction
import com.testsite.reddittop.ui.UiStateHolder
import com.testsite.reddittop.ui.UiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appEventsManager: AppEventsManager,
    private val clientRepository: ClientRepository,
) : UiViewModel<AppState, AppAction, AppEvent>() {

    private val _uiState = MutableStateFlow(UiState.Available(AppState))
    override val uiState: StateFlow<UiState<AppState>>
        get() = _uiState.asStateFlow()

    override val uiEvent: Flow<AppEvent>
        get() = appEventsManager.appEvent

    init {
        clientRepository.authenticate()
            .onEach { token -> action(AppAction.RefreshPage).also { Timber.d(token.toString()) } }
            .launchIn(viewModelScope)
    }

    override fun action(action: AppAction) {
        when (action) {
            AppAction.RefreshPage -> appEventsManager.send(AppEvent.RefreshPage)
        }
    }
}

data object AppState : UiStateHolder
sealed interface AppAction : UiStateAction {
    data object RefreshPage : AppAction
}

sealed interface AppEvent : UiEvent {
    data object RefreshPage : AppEvent
}