package com.rollinup.rollinup.component.permit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PermitRequestBottomSheet(
    private val getPermitByIdUseCase: GetPermitByIdUseCase,
) : ViewModel() {
    fun fetchDetail(
        id: String,
        onLoading: (Boolean) -> Unit,
        onResult: (PermitDetailEntity) -> Unit,
    ) {
        if (id.isBlank()) return
        onLoading(true)
        viewModelScope.launch {
            getPermitByIdUseCase(id).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        onLoading(false)
                    }

                    is Result.Success -> {
                        onResult(result.data)
                        onLoading(false)
                    }
                }
            }
        }
    }

    fun submit(
        isEdit: Boolean,
        detail: PermitDetailEntity,
    ) {

    }

//    fun validateForm():Boolean {
//
//    }

}