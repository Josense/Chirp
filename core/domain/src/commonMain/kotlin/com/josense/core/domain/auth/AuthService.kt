package com.josense.core.domain.auth

import com.josense.core.domain.DataError
import com.josense.core.domain.EmptyResult
import com.josense.core.domain.Result

interface AuthService {
    suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote>

    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote>
}