package com.romiusse.todoapp.screens.main

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romiusse.todoapp.screens.main.snack_bar_msg.MessageStatus
import com.romiusse.todoapp.screens.main.snack_bar_msg.SnackBarMessage
import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.server.transmitter.ServerAnswer
import com.romiusse.todoapp.server.transmitter.ServerErrors
import com.romiusse.todoapp.server.transmitter.ServerStatus
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import com.romiusse.todoapp.utils.Utils.convertClientModelToServer
import com.romiusse.todoapp.utils.Utils.convertServerModelToClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class MainScreenViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val serverTransmitter: ServerTransmitter,
    private val bottomSheetUtils: BottomSheetUtils
    ) : ViewModel() {

    private val _items = MutableLiveData<List<TodoItem>>()
    val items: LiveData<List<TodoItem>> = _items

    private val _bottomItems = MutableLiveData<List<TodoItem>>()
    val bottomItems: LiveData<List<TodoItem>> = _bottomItems

    private val _info = MutableLiveData<SnackBarMessage?>()
    val info: LiveData<SnackBarMessage?> = _info

    private val _isInternetConnected = MutableLiveData(false)
    val isInternetConnected: LiveData<Boolean> = _isInternetConnected

    private val _syncIconStatus = MutableLiveData<SyncIconStatus>()
    val syncIconStatus: LiveData<SyncIconStatus> = _syncIconStatus

    private val _isBottomSheetShow = MutableLiveData(false)
    val isBottomSheetShow: LiveData<Boolean> = _isBottomSheetShow

    private var isSynchronized = false
    private var isPreSynchronized = false

    private var isListInitialized = false


    init {
        setItemsListener()
        setServerDataListener()
        setBottomSheetStateListener()
    }

    fun initToken(token: String?){
        token?.let{serverTransmitter.TOKEN = token}
    }

    private fun setItemsListener(){
        viewModelScope.launch {
            todoItemsRepository.
            getList().collect{ list ->

                //Try to load data from server after list was initialized
                if (!isListInitialized && _isInternetConnected.value!!) loadData()
                isListInitialized = true

                _items.value = list

                if (isSynchronized && _isInternetConnected.value!!
                ) mergeItems(list)

            }
        }
    }
    private fun setServerDataListener(){
        viewModelScope.launch{
            serverTransmitter.listenServerData().collect{
                parseServerData(it)
            }
        }
    }
    private fun setBottomSheetStateListener(){
        viewModelScope.launch {
            bottomSheetUtils.listenBottomSheetState().collect(){
                _isBottomSheetShow.value = it
            }
        }
    }

    private fun mergeItems(list: List<TodoItem>){
        viewModelScope.launch(Dispatchers.IO){
            serverTransmitter.mergeItems(
                ListWrapper(list = list.map { convertClientModelToServer(it) })
            )
        }
    }

    private fun updateAllList(list: List<TodoItem>){
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateAllList(list)
        }
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if(!_isInternetConnected.value!!)
                loadData()
            _isInternetConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isInternetConnected.postValue(false)
            notSynchronized()
        }
    }

    private fun checkAreListsSame(list: List<TodoItem>) = (list == _items.value)

    private fun synchronized(){
        isSynchronized = true
        isPreSynchronized = true
        _info.postValue(SnackBarMessage(status = MessageStatus.DATA_WAS_UPDATED))
        _syncIconStatus.postValue(SyncIconStatus.OK)
    }

    private fun notSynchronized(){
        isSynchronized = false
        isPreSynchronized = false
        _syncIconStatus.postValue(SyncIconStatus.ERROR)
    }

    private fun synchronize(list: List<TodoItem>){

        if(!checkAreListsSame(list) && !isPreSynchronized){
            bottomSheetUtils.bottomSheetShowed()
            return
        }

        if(!checkAreListsSame(list)) updateAllList(list)
        synchronized()
    }

    private fun onSuccess(answer:  ServerAnswer<List<ServerTodoItem>>){

        val list: List<TodoItem> = answer.answer!!.map { convertServerModelToClient(it) }

        if(answer.requestName == "getItems")
            _bottomItems.value = list

        if(!isSynchronized){
            synchronize(list)
            return
        }

        _syncIconStatus.value = SyncIconStatus.OK

        if(!checkAreListsSame(list))
            updateAllList(list)
    }

    private fun onRetry(answer: ServerAnswer<List<ServerTodoItem>>){
        _info.value = SnackBarMessage(status = MessageStatus.RETRYING, suffix = answer.info)
        _syncIconStatus.value = SyncIconStatus.SYNCHRONIZING
    }

    private fun onError(answer: ServerAnswer<List<ServerTodoItem>>){
        val status = when(answer.error){
            ServerErrors.SOCKET_TIME_OUT -> MessageStatus.CONNECTION_TIME_OUT
            ServerErrors.WRONG_AUTH -> MessageStatus.WRONG_AUTH
            else -> MessageStatus.SERVER_ERROR
        }
        _info.value = SnackBarMessage(status = status)
        notSynchronized()
    }

    private fun onLoading(answer: ServerAnswer<List<ServerTodoItem>>){
        _syncIconStatus.value = SyncIconStatus.SYNCHRONIZING
    }

    private fun parseServerData(answer: ServerAnswer<List<ServerTodoItem>>){

        when(answer.status){
            ServerStatus.LOADING    -> onLoading(answer)
            ServerStatus.SUCCESS    -> onSuccess(answer)
            ServerStatus.RETRYING   -> onRetry(answer)
            ServerStatus.ERROR      -> onError(answer)
        }
    }

    private fun loadData(){
        viewModelScope.launch(Dispatchers.IO){
            serverTransmitter.getItems()
        }

    }

    fun updateFromList(todoItem: TodoItem){
        todoItem.changedAt = Date()
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateFromList(todoItem)
        }
    }

    fun setActualData(){
        isPreSynchronized = true
        bottomSheetUtils.bottomSheetClosed()
        mergeItems(_items.value!!)
    }

    fun getActualData(){
        isPreSynchronized = true
        bottomSheetUtils.bottomSheetClosed()
        loadData()
    }

    fun bottomSheetClosed(){
        bottomSheetUtils.bottomSheetClosed()
        notSynchronized()
    }

    fun refresh(){
        if (_isInternetConnected.value!!){
            notSynchronized()
            loadData()
        }
        else _info.value = SnackBarMessage(status = MessageStatus.CONNECTION_LOST)
    }

    fun clearInfo() {
        _info.value = null
    }


}