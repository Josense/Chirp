package com.josense.chirp.di

import com.josense.core.data.di.coreDataModule
import com.josense.feature.auth.presentation.di.authPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(coreDataModule, authPresentationModule)
    }
}