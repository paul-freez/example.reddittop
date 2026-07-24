package com.testsite.reddittop.ui.screen.top

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.testsite.reddittop.ui.UiState
import com.testsite.reddittop.ui.components.Error
import com.testsite.reddittop.ui.components.Loading
import com.testsite.reddittop.ui.model.RedditPostUI
import com.testsite.reddittop.ui.screen.top.components.TopPostCard

@Composable
fun TopPostsScreen(viewModel: TopPostsScreenViewModel, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(lifecycleOwner, viewModel.uiEvent) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is TopPostsEvent.OpenLink -> event.intent.launchUrl(context, event.link)
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState,
        transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        }
    ) { state ->
        when (state) {
            is UiState.Available -> TopPostsScreenLayout(
                posts = state.data.posts,
                onPostClick = {
                    viewModel.action(
                        TopPostsAction.OnPostClick(it)
                    )
                },
                modifier = modifier,
            )

            is UiState.Error -> Error(errorMsg = state.error.message.orEmpty())
            UiState.Loading -> Loading()
        }
    }
}

@Composable
private fun TopPostsScreenLayout(
    posts: List<RedditPostUI>,
    onPostClick: (RedditPostUI) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = posts, key = RedditPostUI::link) { post ->
            TopPostCard(post = post, onPostClicked = { onPostClick(post) })
        }
    }
}