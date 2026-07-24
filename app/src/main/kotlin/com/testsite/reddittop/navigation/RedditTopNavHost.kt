package com.testsite.reddittop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.testsite.reddittop.ui.screen.top.TopPostsDestination

@Composable
fun RedditTopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TopPostsDestination.route,
        modifier = modifier
    ) {
        addGraph()
    }
}
