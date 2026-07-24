package com.testsite.reddittop.ui.screen.top

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.testsite.reddittop.components.managers.AppEventsManager
import com.testsite.reddittop.ui.UIHostedViewModel
import com.testsite.reddittop.ui.UiEvent
import com.testsite.reddittop.ui.UiState
import com.testsite.reddittop.ui.UiStateAction
import com.testsite.reddittop.ui.UiStateHolder
import com.testsite.reddittop.ui.model.RedditPostUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopPostsScreenViewModel @Inject constructor(
    private val tabsIntent : CustomTabsIntent,
    eventsManager: AppEventsManager,
) : UIHostedViewModel<TopPostsUiState, TopPostsAction, TopPostsEvent>(eventsManager) {
    private val _uiState = MutableStateFlow<UiState<TopPostsUiState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<TopPostsUiState>> = _uiState.asStateFlow()

    private val _uiEvent = Channel<TopPostsEvent>()
    override val uiEvent: Flow<TopPostsEvent> = _uiEvent.receiveAsFlow()

    override fun action(action: TopPostsAction) {
        when (action) {
            is TopPostsAction.OnPostClick -> viewModelScope.launch {
                _uiEvent.send(
                    TopPostsEvent.OpenLink(
                        intent = tabsIntent,
                        link = action.redditPostUI.link.toUri()
                    )
                )
            }
        }
    }

    override fun loadContent() {
        TODO("Not yet implemented")
    }
}

data class TopPostsUiState(val posts: List<RedditPostUI>) : UiStateHolder
sealed interface TopPostsAction : UiStateAction {
    data class OnPostClick(val redditPostUI: RedditPostUI) : TopPostsAction
}

sealed interface TopPostsEvent : UiEvent {
    data class OpenLink(val intent: CustomTabsIntent, val link: Uri) : TopPostsEvent
}