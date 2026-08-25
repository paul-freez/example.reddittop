package com.testsite.reddittop.ui.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.testsite.reddittop.R
import com.testsite.reddittop.navigation.NavigationManager
import com.testsite.reddittop.navigation.RedditTopNavHost
import com.testsite.reddittop.navigation.onNavAction
import com.testsite.reddittop.ui.UiState
import com.testsite.reddittop.ui.components.Loading
import com.testsite.reddittop.utils.toUserMessage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditTopApp(
    navigationManager: NavigationManager,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

    val resources = LocalResources.current
    val errorMessage = remember(uiState, resources) { (uiState as? UiState.Error)?.error?.toUserMessage(resources) }
    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            appViewModel.action(AppAction.DismissError)
        }
    }

    AppEffects(
        navigationManager = navigationManager,
        navController = navController
    )

    Scaffold(
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))  // TODO: Add some styling
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = colorResource(R.color.colorPrimary), // TODO: Replace with compose color
                    containerColor = colorResource(R.color.colorPrimary), // TODO: Replace with compose color
                    titleContentColor = colorResource(R.color.colorAccent) // TODO: Replace with compose color
                ),
                scrollBehavior = appBarScrollBehavior
            )
        }
    ) { paddingValues ->
        val pullToRefreshState = rememberPullToRefreshState()

        // Child components are responsible for handling refresh animations
        val isRefreshing by remember { mutableStateOf(false) }

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                appViewModel.action(AppAction.RefreshPage)
            },
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = colorResource(R.color.colorAccent), // TODO: Replace with compose color
                    color = colorResource(R.color.colorPrimary), // TODO: Replace with compose color
                    state = pullToRefreshState
                )
            }
        ) {
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn().togetherWith(fadeOut())
                },
                label = "AppContent"
            ) { state ->
                if (state is UiState.Available<*>) {
                    RedditTopNavHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Errors are handled in the snackbar!
                    Loading()
                }
            }
        }
    }
}

@Composable
private fun AppEffects(
    navigationManager: NavigationManager,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        navigationManager.navActions
            .collect { action -> navController.onNavAction(action) }
    }
}