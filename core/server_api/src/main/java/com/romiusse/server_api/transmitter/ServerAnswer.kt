package com.romiusse.server_api.transmitter

import com.romiusse.todoapp.server.transmitter.ServerStatus

data class ServerAnswer<T>(
    val status: ServerStatus,
    val answer: T?,
    val requestName: String?,
    val error: ServerErrors?,
    val info: String?
)