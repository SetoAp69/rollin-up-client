package com.rollinup.apiservice.domain.generalsetting

import com.rollinup.apiservice.data.repository.generalsetting.GeneralSettingRepository

class ListenGeneralSettingSSE(
    private val repository: GeneralSettingRepository,
) {
    operator fun invoke() = repository.listen()
}