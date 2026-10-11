package com.josense.core.domain.auth

import com.josense.core.domain.DataError
import com.josense.core.domain.EmptyResult

interface AuthService {
    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote>
}