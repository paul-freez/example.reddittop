package com.testsite.reddittop.ui.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.testsite.reddittop.R
import com.testsite.reddittop.navigation.NavigationManager
import com.testsite.reddittop.navigation.RedditTopNavHost
import com.testsite.reddittop.navigation.onNavAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditTopApp(
    navigationManager: NavigationManager,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    AppEffects(
        navigationManager = navigationManager,
        navController = navController
    )

    Scaffold(
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.colorPrimary),
                    titleContentColor = colorResource(R.color.colorAccent)
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
                    containerColor = colorResource(R.color.colorAccent),
                    color = colorResource(R.color.colorPrimary),
                    state = pullToRefreshState
                )
            }
        ) {
            RedditTopNavHost(navController = navController, Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()))
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