package com.josense.core.data.auth

import com.josense.core.data.dto.AuthInfoSerializable
import com.josense.core.data.dto.request.EmailRequest
import com.josense.core.data.dto.request.LoginRequest
import com.josense.core.data.dto.request.RegisterRequest
import com.josense.core.data.mappers.toDomain
import com.josense.core.data.networking.get
import com.josense.core.data.networking.post
import com.josense.core.domain.DataError
import com.josense.core.domain.EmptyResult
import com.josense.core.domain.Result
import com.josense.core.domain.auth.AuthInfo
import com.josense.core.domain.auth.AuthService
import com.josense.core.domain.map
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient,
): AuthService {
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(
        email: String,
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/resend",
            body = EmailRequest(email)
        )
    }

    override suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }
}