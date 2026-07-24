package com.testsite.reddittop.components.managers

import com.testsite.reddittop.components.concurrency.AppDispatchers
import com.testsite.reddittop.ui.app.AppEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppEventsManager @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val externalScope: CoroutineScope
) {
    private val _appEvent = MutableSharedFlow<AppEvent>()
    val appEvent: SharedFlow<AppEvent> = _appEvent.asSharedFlow()

    fun send(appEvent: AppEvent) {
        externalScope.launch(dispatchers.Default) {
            _appEvent.emit(appEvent)
        }
    }
}