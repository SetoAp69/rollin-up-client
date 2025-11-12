package com.rollinup.apiservice.data.repository.pagging

import androidx.paging.PagingData
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import kotlinx.coroutines.flow.Flow

interface PagingDummyRepository {
    fun getPagingData(): Flow<PagingData<PagingDummyEntity>>
}