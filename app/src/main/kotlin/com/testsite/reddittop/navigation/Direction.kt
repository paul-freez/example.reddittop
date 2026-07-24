package com.testsite.reddittop.navigation

import android.os.Bundle
import androidx.navigation.NavOptions

/**
 * Holder class for navigation data
 */
data class Direction(
    val path: String,
    val args: Bundle,
    val navOptions: NavOptions,
)
