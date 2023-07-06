package com.romiusse.todo_list.screen.snack_bar_msg

import com.romiusse.todoapp.screens.main.snack_bar_msg.MessageStatus


data class SnackBarMessage(

    val status: MessageStatus,
    val prefix: String? = null,
    val suffix: String? = null
){
    override fun toString(): String {
        return prefix + status.toString() + suffix
    }
}