package com.rollinup.apiservice.repository.generalsetting

import com.rollinup.apiservice.Utils
import com.rollinup.apiservice.mapper.GeneralSettingMapper
import com.rollinup.apiservice.model.GeneralSetting
import com.rollinup.apiservice.model.NetworkError
import com.rollinup.apiservice.model.Result
import com.rollinup.apiservice.source.network.apiservice.GeneralSettingApiService
import com.rollinup.apiservice.source.network.model.response.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GeneralSettingRepositoryImpl(
    private val dataSource: GeneralSettingApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val mapper: GeneralSettingMapper,
) : GeneralSettingRepository {
    override fun listen(): Flow<Result<GeneralSetting, NetworkError>> = flow {
        dataSource.listen().collect { response ->
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    val data = mapper.mapGeneralSetting(response.data)

                    emit(Result.Success(data))
                }
            }
        }
    }
        .catch { e -> emit(Utils.handleApiError(e as Exception)) }
        .flowOn(ioDispatcher)
}