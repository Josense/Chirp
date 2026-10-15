package com.josense.feature.auth.presentation.di

import com.josense.feature.auth.presentation.email_verification.EmailVerificationViewModel
import com.josense.feature.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.josense.feature.auth.presentation.login.LoginViewModel
import com.josense.feature.auth.presentation.register.RegisterViewModel
import com.josense.feature.auth.presentation.register_success.RegisterSuccessViewModel
import com.josense.feature.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}