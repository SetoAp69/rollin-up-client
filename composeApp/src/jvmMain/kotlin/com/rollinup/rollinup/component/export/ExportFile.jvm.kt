package com.rollinup.rollinup.component.export

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun fileExporterModule(): Module {
    return module {
        singleOf<FileWriter>(::JVMFileWriter)
    }
}