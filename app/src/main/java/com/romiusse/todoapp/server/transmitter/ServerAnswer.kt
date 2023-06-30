package com.romiusse.todoapp.server.transmitter

data class ServerAnswer<T>(
    val status: ServerStatus,
    val answer: T?,
    val requestName: String?,
    val error: ServerErrors?,
    val info: String?
)