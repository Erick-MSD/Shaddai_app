package com.example.shaddai_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform