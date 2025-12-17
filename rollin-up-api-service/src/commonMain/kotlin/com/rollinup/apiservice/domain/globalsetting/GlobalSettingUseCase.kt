package com.rollinup.apiservice.domain.globalsetting

import com.rollinup.apiservice.data.repository.generalsetting.GlobalSettingRepository
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody

class ListenGlobalSettingSSE(
    private val repository: GlobalSettingRepository,
) {
    operator fun invoke() = repository.listen()
}

class GetCachedGlobalSettingUseCase(
    private val  repository : GlobalSettingRepository
){
    suspend operator fun invoke() = repository.getCachedGlobalSetting()
}

class GetGlobalSettingUseCase(
    private val repository: GlobalSettingRepository,
) {
    operator fun invoke() = repository.getGlobalSetting()
}

class EditGlobalSettingUseCase(
    private val repository: GlobalSettingRepository,
) {
    operator fun invoke(body: EditGlobalSettingBody) = repository.editGlobalSetting(body)
}