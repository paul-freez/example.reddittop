package com.testsite.reddittop.navigation

import com.testsite.reddittop.components.concurrency.AppDispatchers
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class NavigationManager @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val externalScope: CoroutineScope
) {
    private val _navActions = MutableSharedFlow<NavAction>()
    val navActions: SharedFlow<NavAction> = _navActions.asSharedFlow()

    fun goBack() {
        send(NavAction.Back)
    }

    fun goTo(directions: Direction) {
        send(NavAction.GoTo(directions))
    }

    private fun send(action: NavAction) {
        externalScope.launch(dispatchers.Main) {
            _navActions.emit(action)
        }
    }
}
