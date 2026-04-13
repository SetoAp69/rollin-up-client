package com.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CounterViewModel() : ViewModel() {
    private val duration = 5 * 60 * 1000
    private val _counter = MutableStateFlow(duration)
    val counter = _counter.asStateFlow()

    private var countDownJob: Job? = null

    fun startTimer(onTimeOut: () -> Unit) {
        countDownJob?.cancel()
        countDownJob = viewModelScope.launch {
            while (counter.value > 0) {
                delay(1000)
                _counter.value -= 1000
                L.wtf{
                    """
                        Countdown start : ${counter.value / 1000} seconds remaining
                    """.trimIndent()
                }
                if (counter.value <= 0) {
                    onTimeOut()
                }
            }
        }
    }

    fun stopTimer() {
        countDownJob?.cancel()
        _counter.value = duration
    }
}