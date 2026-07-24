package com.testsite.reddittop.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.testsite.reddittop.ui.screen.top.TopPostsDestination
import com.testsite.reddittop.ui.screen.top.TopPostsScreen

fun NavGraphBuilder.addGraph() {
    composable(route = TopPostsDestination.route) {
        TopPostsScreen(
            viewModel = hiltViewModel()
        )
    }
}