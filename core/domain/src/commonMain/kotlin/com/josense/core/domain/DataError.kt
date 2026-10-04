package com.josense.core.domain

sealed interface DataError: Error {
    enum class Remote {
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        SERIALIZATION,
        UNKNOWN,
        ;
    }

    enum class Local {
        DISK_FULL,
        FILE_NOT_FOUND,
        UNKNOWN,
        ;
    }
}