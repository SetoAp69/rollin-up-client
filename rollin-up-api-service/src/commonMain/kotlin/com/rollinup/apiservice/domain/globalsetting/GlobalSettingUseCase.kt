package com.rollinup.apiservice.domain.globalsetting

import com.rollinup.apiservice.data.repository.generalsetting.GlobalSettingRepository
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.model.common.GlobalSetting

class ListenGlobalSettingSSE(
    private val repository: GlobalSettingRepository,
) {
    operator fun invoke() = repository.listen()
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

class GetCachedGlobalSettingUseCase(
    private val repository: GlobalSettingRepository,
) {
    suspend operator fun invoke() = repository.getCachedGlobalSetting()
}

class UpdateCachedGlobalSettingUseCase(
    private val repository: GlobalSettingRepository,
) {
    suspend operator fun invoke(globalSetting: GlobalSetting) =
        repository.updateCachedGlobalSetting(globalSetting)
}
