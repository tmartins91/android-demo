package com.tmartins.feature_currencies

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
public class ViewModelFlowCollector<S, E>(
    private val stateFlow: Flow<S>,
    private val eventFlow: Flow<E>,
    private val dispatcher: TestCoroutineDispatcher
) {

    public fun test(test: (List<S>, List<E>) -> Unit): Unit = dispatcher.runBlockingTest {
        val states = mutableListOf<S>()
        val stateJob = launch { stateFlow.toList(states) }
        val events = mutableListOf<E>()
        val eventJob = launch { eventFlow.toList(events) }
        test(states, events)
        stateJob.cancel()
        eventJob.cancel()
    }
}

public val NO_STATE: Flow<Unit> = MutableStateFlow(Unit)
public val NO_EVENTS: Flow<Unit> = MutableSharedFlow<Unit>()