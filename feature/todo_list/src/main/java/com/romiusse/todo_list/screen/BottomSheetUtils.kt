package com.romiusse.todo_list.screen

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

typealias BottomSheetStateListener = (state: Boolean) -> Unit

/**
 *
 * Utils for bottom sheet utils
 *
 * @author Romiusse
 */
class BottomSheetUtils {

    private val bottomSheetStateListeners = mutableSetOf<BottomSheetStateListener>()

    fun listenBottomSheetState() = callbackFlow{
        val listener: BottomSheetStateListener = {trySend(it)}
        bottomSheetStateListeners.add(listener)
        awaitClose { bottomSheetStateListeners.remove(listener) }
    }

    fun bottomSheetClosed(){
        bottomSheetStateListeners.forEach { it.invoke(false) }
    }

    fun bottomSheetShowed(){
        bottomSheetStateListeners.forEach { it.invoke(true) }
    }



}
