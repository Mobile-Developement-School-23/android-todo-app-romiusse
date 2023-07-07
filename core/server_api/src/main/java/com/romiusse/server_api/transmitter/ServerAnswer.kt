package com.romiusse.server_api.transmitter

import com.romiusse.todoapp.server.transmitter.ServerStatus

/**
 * Server answer class
 *
 * This class contains answer state ans other useful info
 *
 * @param T the type of a server answer
 * @author Romiusse
 */
data class ServerAnswer<T>(
    val status: ServerStatus,
    val answer: T?,
    val requestName: String?,
    val error: ServerErrors?,
    val info: String?
)