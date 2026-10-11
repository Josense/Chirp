package com.josense.core.data.auth

import com.josense.core.data.dto.request.RegisterRequest
import com.josense.core.data.networking.post
import com.josense.core.domain.DataError
import com.josense.core.domain.EmptyResult
import com.josense.core.domain.auth.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class KtorAuthService(
    private val httpClient: HttpClient,
): AuthService {
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
}