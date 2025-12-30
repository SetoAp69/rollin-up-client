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

/**
 * Implementation of the [GlobalSettingRepository] interface.
 *
 * This repository manages global application settings, synchronizing them between
 * the remote server and local storage ([LocalDataStore]). It supports fetching,
 * editing, and listening for real-time updates.
 *
 * @property apiDataSource The API service for fetching/updating settings remotely.
 * @property localDataSource The local data store for caching settings.
 * @property ioDispatcher The CoroutineDispatcher for performing IO operations.
 * @property mapper The mapper to transform network DTOs into domain entities.
 */
class GlobalSettingRepositoryImpl(
    private val apiDataSource: GlobalSettingApiService,
    private val localDataSource: LocalDataStore,
    private val ioDispatcher: CoroutineDispatcher,
    private val mapper: GlobalSettingMapper,
) : GlobalSettingRepository {

    /**
     * Establishes a listener for real-time updates to global settings from the server.
     *
     * @return A Flow that emits [Unit] whenever a successful update notification is received.
     */
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

    /**
     * Fetches the current global settings from the remote server.
     *
     * @return A Flow emitting a [Result] containing [GlobalSetting] on success or [NetworkError] on failure.
     */
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

    /**
     * Updates the global settings on the server.
     *
     * @param body The request body containing the updated settings values.
     * @return A Flow emitting [Result.Success] on completion or [NetworkError] on failure.
     */
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

    /**
     * Retrieves the global settings currently cached in local storage.
     *
     * @return The cached [GlobalSetting] object, or null if none exists.
     */
    override suspend fun getCachedGlobalSetting(): GlobalSetting? {
        return localDataSource.getLocalGlobalSetting()
    }

    /**
     * Updates the local cache with new global settings.
     *
     * @param globalSetting The new settings object to cache.
     */
    override suspend fun updateCachedGlobalSetting(globalSetting: GlobalSetting) {
        localDataSource.updateGlobalSetting(globalSetting)
    }
}