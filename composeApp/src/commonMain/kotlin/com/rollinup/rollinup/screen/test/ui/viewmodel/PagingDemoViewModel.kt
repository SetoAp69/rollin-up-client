package com.rollinup.rollinup.screen.test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.pagging.GetPagingDummyUseCase
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import com.rollinup.rollinup.screen.test.ui.uistate.PagingDummyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PagingDemoViewModel(
    private val getPagingDummyUseCase: GetPagingDummyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PagingDummyUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<PagingDummyEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init() {
        viewModelScope.launch {
            getPagingDummyUseCase.invoke().collect {
                _pagingData.value = it
            }
        }
    }


    fun refresh() {
        _pagingData.value = PagingData.empty()
        init()
    }

}