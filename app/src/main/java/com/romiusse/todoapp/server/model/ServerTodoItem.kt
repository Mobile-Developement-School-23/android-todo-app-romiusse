package com.romiusse.todoapp.server.model

import com.google.gson.annotations.SerializedName

data class ServerTodoItem(

    @SerializedName("id"              ) var id            : String,
    @SerializedName("text"            ) var text          : String,
    @SerializedName("importance"      ) var importance    : String,
    @SerializedName("deadline"        ) var deadline      : Long?     = null,
    @SerializedName("done"            ) var done          : Boolean,
    @SerializedName("color"           ) var color         : String?  = null,
    @SerializedName("created_at"      ) var createdAt     : Long,
    @SerializedName("changed_at"      ) var changedAt     : Long,
    @SerializedName("last_updated_by" ) var lastUpdatedBy : String?  = null

)