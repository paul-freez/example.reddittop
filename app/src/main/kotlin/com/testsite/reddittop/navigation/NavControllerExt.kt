package com.testsite.reddittop.navigation

import androidx.navigation.NavController

fun NavController.onNavAction(navAction: NavAction) {
    when (navAction) {
        NavAction.Back -> popBackStack()
        is NavAction.GoTo -> navigate(direction = navAction.direction)
    }
}

fun NavController.navigate(direction: Direction) {
    val nodeId = graph.findNode(direction.path)?.id
    if (nodeId != null) {
        navigate(
            resId = nodeId,
            args = direction.args,
            navOptions = direction.navOptions,
        )
    } else {
        navigate(direction.path, direction.navOptions)
    }
}