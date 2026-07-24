package com.testsite.reddittop.navigation

sealed interface NavAction {
    data object Back : NavAction
    data class GoTo(val direction: Direction) : NavAction
}