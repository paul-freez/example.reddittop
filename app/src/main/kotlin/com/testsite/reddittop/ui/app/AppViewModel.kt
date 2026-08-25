package com.testsite.reddittop.ui.app

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
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appEventsManager: AppEventsManager,
    clientRepository: ClientRepository,
) : UiViewModel<AppState, AppAction, AppEvent>() {

    private val _uiState = MutableStateFlow<UiState<AppState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<AppState>>
        get() = _uiState.asStateFlow()

    override val uiEvent: Flow<AppEvent>
        get() = appEventsManager.appEvent

    init {
        clientRepository.authenticate()
            .onEach { token ->
                _uiState.update { UiState.Available(AppState) }
                action(AppAction.RefreshPage).also { Timber.d(token.toString()) }
            }
            .launchIn(baseViewModelScope)
    }

    override fun action(action: AppAction) {
        when (action) {
            AppAction.RefreshPage -> appEventsManager.send(AppEvent.RefreshPage)
            AppAction.DismissError -> _uiState.update { UiState.Available(AppState)}
        }
    }

    override fun handleException(throwable: Throwable) {
        _uiState.update { UiState.Error(throwable) }
    }
}

data object AppState : UiStateHolder
sealed interface AppAction : UiStateAction {
    data object RefreshPage : AppAction
    data object DismissError : AppAction
}

sealed interface AppEvent : UiEvent {
    data object RefreshPage : AppEvent
}