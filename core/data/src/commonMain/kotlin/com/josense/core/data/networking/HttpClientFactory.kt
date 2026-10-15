package com.josense.core.data.networking

import com.josense.core.data.BuildKonfig
import com.josense.core.data.dto.AuthInfoSerializable
import com.josense.core.data.dto.request.RefreshRequest
import com.josense.core.data.mappers.toDomain
import com.josense.core.domain.DataError
import com.josense.core.domain.auth.SessionStorage
import com.josense.core.domain.logging.ChirpLogger
import com.josense.core.domain.onFailure
import com.josense.core.domain.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parseServerSetCookieHeader
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val chirpLogger: ChirpLogger,
    private val sessionStorage: SessionStorage,
) {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            // 开启 Ktor 的 JSON 自动转换功能，并且设置当后台返回多余字段时不要报错崩溃。
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        chirpLogger.debug(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage.observeAuthInfo()
                            .firstOrNull()
                            ?.let {
                                BearerTokens(
                                    accessToken = it.accessToken,
                                    refreshToken = it.refreshToken,
                                )
                            }
                    }
                    refreshTokens {
                        if (response.request.url.encodedPath.contains("auth")) {
                            return@refreshTokens null
                        }
                        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
                        if (authInfo?.refreshToken.isNullOrBlank()) {
                            sessionStorage.set(null)
                            return@refreshTokens null
                        }
                        var bearerTokens: BearerTokens? = null
                        client.post<RefreshRequest, AuthInfoSerializable>(
                            route = "auth/refresh",
                            body = RefreshRequest(refreshToken = authInfo.refreshToken),
                            builder = {
                                markAsRefreshTokenRequest()
                            }
                        ).onSuccess { newAuthInfo ->
                            sessionStorage.set(newAuthInfo.toDomain())
                            bearerTokens = BearerTokens(
                                accessToken = newAuthInfo.accessToken,
                                refreshToken = newAuthInfo.refreshToken,
                            )
                        }.onFailure {
                            sessionStorage.set(null)
                        }
                        bearerTokens
                    }
                }
            }
        }
    }
}