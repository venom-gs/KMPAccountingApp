package org.example.kmpaccountingapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform