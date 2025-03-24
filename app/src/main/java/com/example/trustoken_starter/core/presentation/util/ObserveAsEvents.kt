package com.example.trustoken_starter.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
/**
 * A Composable helper function to observe a Flow as events in a lifecycle-aware manner.
 *
 * @param T The type of events emitted by the Flow.
 * @param events The Flow emitting events that should be observed.
 * @param key1 An optional key to control recomposition.
 * @param key2 An optional secondary key for recomposition control.
 * @param onEvent A callback function invoked whenever a new event is emitted.
 */
@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    // Get the current LifecycleOwner for observing lifecycle events
    val lifecycleOwner = LocalLifecycleOwner.current

    // Launch an effect tied to the lifecycle state and provided keys
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        // Ensure collection happens only when the lifecycle is at least STARTED
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                // Collect events and invoke the provided callback
                events.collect(onEvent)
            }
        }
    }
}
