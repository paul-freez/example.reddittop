package com.testsite.reddittop.ui.screen.top

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.testsite.reddittop.components.managers.AppEventsManager
import com.testsite.reddittop.core.ExceptionHandler
import com.testsite.reddittop.data.posts.repo.FeedRepository
import com.testsite.reddittop.ui.UIHostedViewModel
import com.testsite.reddittop.ui.UiEvent
import com.testsite.reddittop.ui.UiMapper.toUI
import com.testsite.reddittop.ui.UiState
import com.testsite.reddittop.ui.UiStateAction
import com.testsite.reddittop.ui.UiStateHolder
import com.testsite.reddittop.ui.model.RedditPostUI
import com.testsite.reddittop.ui.screen.top.TopPostsEvent.OpenLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TopPostsScreenViewModel @Inject constructor(
    private val exceptionHandler: ExceptionHandler,
    private val tabsIntent: CustomTabsIntent,
    private val feedRepository: FeedRepository,
    eventsManager: AppEventsManager,
) : UIHostedViewModel<TopPostsUiState, TopPostsAction, TopPostsEvent>(exceptionHandler, eventsManager) {
    private val _uiState = MutableStateFlow<UiState<TopPostsUiState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<TopPostsUiState>> = _uiState.asStateFlow()

    private val _uiEvent = Channel<TopPostsEvent>()
    override val uiEvent: Flow<TopPostsEvent> = _uiEvent.receiveAsFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    val posts: Flow<PagingData<RedditPostUI>> = refreshTrigger
        .flatMapLatest {
            feedRepository.getTopPostsPager()
                .map { pagingData -> pagingData.map { post -> post.toUI() } }
        }
        .cachedIn(viewModelScope)

    override fun action(action: TopPostsAction) {
        when (action) {
            is TopPostsAction.OnPostClick -> viewModelScope.launch {
                _uiEvent.send(
                    OpenLink(
                        intent = tabsIntent,
                        link = action.redditPostUI.link.toUri()
                    )
                )
            }

            is TopPostsAction.OnPostsStateUpdate -> _uiState.update {
                when (val newState = action.state) {
                    is UiState.Error -> UiState.Error(exceptionHandler.updateException(newState.error))
                    else -> newState
                }
            }

        }
    }

    override fun loadContent() {
        _uiState.update { UiState.Loading }
        refreshTrigger.tryEmit(Unit)
    }
}

data object TopPostsUiState : UiStateHolder
sealed interface TopPostsAction : UiStateAction {
    data class OnPostClick(val redditPostUI: RedditPostUI) : TopPostsAction

    data class OnPostsStateUpdate(val state: UiState<TopPostsUiState>) : TopPostsAction
}

sealed interface TopPostsEvent : UiEvent {
    data class OpenLink(val intent: CustomTabsIntent, val link: Uri) : TopPostsEvent
}