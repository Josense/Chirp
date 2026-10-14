package com.josense.core.data.mappers

import com.josense.core.data.dto.AuthInfoSerializable
import com.josense.core.data.dto.UserSerializable
import com.josense.core.domain.auth.AuthInfo
import com.josense.core.domain.auth.User

fun AuthInfoSerializable.toDomain(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toDomain()
    )
}

fun UserSerializable.toDomain(): User {
    return User(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}