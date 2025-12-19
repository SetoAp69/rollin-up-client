package com.rollinup.apiservice.data.repository.generalsetting

import com.rollinup.apiservice.data.mapper.GlobalSettingMapper
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GlobalSettingRepositoryImpl(
    private val apiDataSource: GlobalSettingApiService,
    private val localDataSource: LocalDataStore,
    private val ioDispatcher: CoroutineDispatcher,
    private val mapper: GlobalSettingMapper,
) : GlobalSettingRepository {

    override fun listen(): Flow<Unit> =
        flow {
            apiDataSource.listen().collect { response ->
                when (response) {
                    is ApiResponse.Error -> {}

                    is ApiResponse.Success -> {
                        emit(Unit)
                    }
                }
            }
        }
            .catch { e -> e.printStackTrace() }
            .flowOn(ioDispatcher)

    override fun getGlobalSetting(): Flow<Result<GlobalSetting, NetworkError>> =
        flow {
            val response = apiDataSource.getGlobalSetting()
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    val data = mapper.mapGetGlobalSettingResponse(response.data.data)
                    emit(Result.Success(data))
                }
            }
        }
            .catch { e -> emit(Utils.handleApiError(e as Exception)) }
            .flowOn(ioDispatcher)

    override fun editGlobalSetting(body: EditGlobalSettingBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = apiDataSource.editGlobalSetting(body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }
            }
        }
            .catch { e -> emit(Utils.handleApiError(e as Exception)) }
            .flowOn(ioDispatcher)

    override suspend fun getCachedGlobalSetting(): GlobalSetting? {
        return localDataSource.getLocalGlobalSetting()
    }

    override suspend fun updateCachedGlobalSetting(globalSetting: GlobalSetting) {
        localDataSource.updateGlobalSetting(globalSetting)
    }
}
