package com.romiusse.server_api.transmitter

/**
 *
 * Enum of server errors
 *
 * @author Romiusse
 */
enum class ServerErrors {
    SOCKET_TIME_OUT,
    WRONG_REQUEST,
    WRONG_AUTH,
    ELEMENT_NOT_FOUND,
    SERVER_ERROR
}