package com.testsite.reddittop.navigation

import android.os.Bundle
import androidx.navigation.NavOptions

abstract class Destination {
    protected abstract val root: String

    open val route: String
        get() = root

    protected open val navOptions = NavOptions.Builder().build()

    open fun prepareDestination(extras: Bundle = Bundle()): Direction = Direction(path = route, args = extras, navOptions = navOptions)
}