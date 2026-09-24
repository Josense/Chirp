package com.josense.chirp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform