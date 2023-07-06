package com.romiusse.todo_list.screen.snack_bar_msg


data class SnackBarMessage(

    val status: MessageStatus,
    val prefix: String? = null,
    val suffix: String? = null
){
    override fun toString(): String {
        return prefix + status.toString() + suffix
    }
}