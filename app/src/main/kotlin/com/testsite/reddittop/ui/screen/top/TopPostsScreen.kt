package com.testsite.reddittop.ui.screen.top

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.testsite.reddittop.R
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
    val posts = viewModel.posts.collectAsLazyPagingItems()

    LaunchedEffect(posts.loadState.refresh, posts.itemCount) {
        val refreshState = posts.loadState.refresh
        viewModel.action(
            /*
             * Workaround to make sure our UiState keep the SSoT for state availability and errors
             */
            TopPostsAction.OnPostsStateUpdate(
                when {
                    refreshState is LoadState.Error -> UiState.Error(refreshState.error)
                    refreshState is LoadState.Loading && posts.itemCount == 0 -> UiState.Loading    // Initial loading
                    else -> UiState.Available(TopPostsUiState)
                }
            )
        )
    }

    AnimatedContent(
        targetState = uiState,
        transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        },
        label = "TopPostsScreenContent"
    ) { state ->
        when (state) {
            is UiState.Available -> {
                TopPostsScreenLayout(
                    posts = posts,
                    onPostClick = {
                        viewModel.action(
                            TopPostsAction.OnPostClick(it)
                        )
                    },
                    modifier = modifier,
                )
            }

            is UiState.Error -> Error(errorMsg = state.error.message.orEmpty())
            UiState.Loading -> Loading()
        }
    }
}

@Composable
private fun TopPostsScreenLayout(
    posts: LazyPagingItems<RedditPostUI>,
    onPostClick: (RedditPostUI) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            count = posts.itemCount,
            key = posts.itemKey { it.link },
        ) { index ->
            posts[index]?.let { post ->
                TopPostCard(post = post, onPostClicked = { onPostClick(post) })
            }
        }

        if (posts.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.colorPrimary)
                    )
                }
            }
        }
    }
}