package com.rollinup.rollinup.component.di

import com.rollinup.rollinup.component.location.LocationViewModel
import com.rollinup.rollinup.component.location.getLocator
import com.rollinup.rollinup.component.permitform.viewmodel.PermitFormViewModel
import com.rollinup.rollinup.component.profile.profilepopup.viemodel.ProfileDialogViewModel
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.Locator
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ComponentModule {
    operator fun invoke() = module {
        single<Geolocator>{
            getLocator()!!
        }
        viewModelOf(::PermitFormViewModel)
        viewModelOf(::ProfileDialogViewModel)
        viewModelOf(::LocationViewModel)
    }
}