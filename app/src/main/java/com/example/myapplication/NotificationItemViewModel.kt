package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class NotificationItemViewModel() : ViewModel() {
    enum class State {
        EXPAND, COLLAPSE,
    }

    private val _collapse = MutableLiveData<Unit>()
    val collapse: LiveData<Unit>
        get() = _collapse
    private val _expand = MutableLiveData<Unit>()
    val expand: LiveData<Unit>
        get() = _expand

    // show/hide
    private val _hide = MutableLiveData<Unit>()
    val hide: LiveData<Unit>
        get() = _hide
    private val _show = MutableLiveData<Unit>()
    val show: LiveData<Unit>
        get() = _show

    private var currentState: State by Delegates.observable(State.COLLAPSE) { _: KProperty<*>, oldState: State, newState: State ->
        when(oldState to newState) {
            (State.COLLAPSE to State.EXPAND) -> {
                _expand.value = Unit
            }
            (State.EXPAND to State.COLLAPSE) -> {
                _collapse.value = Unit
            }
        }
    }

    fun toggle() {
        if (currentState == State.COLLAPSE) {
            currentState = State.EXPAND
        } else if (currentState == State.EXPAND) {
            currentState = State.COLLAPSE
        }
    }

}