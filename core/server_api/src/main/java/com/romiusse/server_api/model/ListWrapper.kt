package com.romiusse.server_api.model

import com.google.gson.annotations.SerializedName

/**
 * Wrapper for server data
 *
 * @author Romiusse
 */
data class ListWrapper(

    @SerializedName("status"   ) var status: String?         = null,
    @SerializedName("list"     ) var list: List<ServerTodoItem> = listOf(),
    @SerializedName("revision" ) var revision: Long?            = null

)
