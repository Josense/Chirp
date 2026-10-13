package com.josense.feature.auth.presentation.di

import com.josense.feature.auth.presentation.email_verification.EmailVerificationViewModel
import com.josense.feature.auth.presentation.register.RegisterViewModel
import com.josense.feature.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
}