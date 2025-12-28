package com.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CounterViewModel() : ViewModel() {
    val duration = 5 * 60 * 1000
    private val _counter = MutableStateFlow(duration)
    val counter = _counter.asStateFlow()

    private var startCounting = false

    fun startTimer(onTimeOut: () -> Unit) {
        startCounting = true
        viewModelScope.launch {
            if (startCounting) {
                while (counter.value > 0) {
                    delay(1)
                    _counter.value = _counter.value - 1
                }
            }
            onTimeOut()
            startCounting = false
        }
    }

    fun stopTimer() {
        startCounting = false
        _counter.value = duration
    }
}