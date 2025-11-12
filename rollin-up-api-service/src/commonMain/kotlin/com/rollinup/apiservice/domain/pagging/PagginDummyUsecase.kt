package com.rollinup.apiservice.domain.pagging

import androidx.paging.PagingData
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import com.rollinup.apiservice.data.repository.pagging.PagingDummyRepository
import kotlinx.coroutines.flow.Flow

class GetPagingDummyUseCase(private val repository: PagingDummyRepository) {
    operator fun invoke(): Flow<PagingData<PagingDummyEntity>> {
        return repository.getPagingData()
    }
}