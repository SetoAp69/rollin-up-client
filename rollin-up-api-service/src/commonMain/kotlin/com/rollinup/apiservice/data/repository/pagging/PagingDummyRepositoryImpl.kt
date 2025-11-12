package com.rollinup.apiservice.data.repository.pagging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import com.rollinup.apiservice.data.repository.pagging.pagingsource.DummyPagignSource
import com.rollinup.apiservice.data.source.network.apiservice.PagingDummyApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class PagingDummyRepositoryImpl(
    private val dataSource: PagingDummyApi,
    private val ioDispatcher: CoroutineDispatcher,
) : PagingDummyRepository {

    override fun getPagingData(): Flow<PagingData<PagingDummyEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = {
                DummyPagignSource(
                    dataSource = dataSource
                )
            }
        ).flow

}