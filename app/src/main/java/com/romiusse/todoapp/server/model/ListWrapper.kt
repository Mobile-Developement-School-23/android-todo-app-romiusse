package com.romiusse.todoapp.server.model

import com.google.gson.annotations.SerializedName

data class ListWrapper(

    @SerializedName("status"   ) var status   : String?         = null,
    @SerializedName("list"     ) var list     : ArrayList<ServerTodoItem> = arrayListOf(),
    @SerializedName("revision" ) var revision : Long?            = null

)