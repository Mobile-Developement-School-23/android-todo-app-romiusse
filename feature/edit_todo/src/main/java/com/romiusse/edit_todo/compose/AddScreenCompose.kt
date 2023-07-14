@file:OptIn(ExperimentalMaterial3Api::class)

package com.romiusse.edit_todo.compose

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.romiusse.edit_todo.R
import com.romiusse.edit_todo.screen.AddScreenViewModel
import com.romiusse.utils.Utils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddScreenCompose {

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
    @Composable
    fun test(
        viewModel: AddScreenViewModel?,
        deleteListener: () -> Unit = {},
        saveListener: () -> Unit = {},
        closeListener: () -> Unit = {},
        switchOnclickListener: (Boolean) -> Unit = {},
        calendarShowListener: () -> Unit = {}
    ){

        AppTheme(){

            Layout(viewModel, deleteListener, saveListener, closeListener, switchOnclickListener,
                calendarShowListener)
        }

    }



    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Layout(viewModel: AddScreenViewModel?,
               deleteListener: () -> Unit,
               saveListener: () -> Unit,
               closeListener: () -> Unit,
               switchOnclickListener: (Boolean) -> Unit,
               calendarShowListener: () -> Unit) {

        var scrollState = rememberScrollState()

        var elevation: Dp = min(16.dp, (scrollState.value * 0.3).toInt().dp)

        Scaffold(topBar = {AppBar(elevation, saveListener, closeListener )}){
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Box(modifier = Modifier.padding(top = 64.dp)){}
                EditText(viewModel)
                Priority(viewModel)
                Divider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))
                Deadline(viewModel, switchOnclickListener, calendarShowListener)
                Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                Delete(deleteListener)
                Box(modifier = Modifier.padding(bottom = 264.dp)){}
            }
        }

    }

    @Composable
    fun Delete(deleteListener: () -> Unit){
        Row{
            Icon(painter = painterResource(id = R.drawable.delete),
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                tint = colorResource(id = R.color.color_red))

            Text(text = stringResource(id = R.string.delete),
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { deleteListener.invoke() },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.color_red))
        }
    }

    @Composable
    fun AppBar(elevation: Dp, saveListener: () -> Unit, closeListener: () -> Unit) {

        Surface(
            shadowElevation = elevation,
        ) {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "")
                    },
                    navigationIcon = {
                        Icon(painter = painterResource(id = R.drawable.close),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clip(RoundedCornerShape(64.dp))
                                .clickable { closeListener.invoke() })
                    },
                    actions = {

                        Text(text = stringResource(id = R.string.save),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { saveListener.invoke() },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.color_blue))
                    },

                    )
            }
        }

    }

    @Composable
    fun EditText(viewModel: AddScreenViewModel?) {
        var inputvalue = remember {
            mutableStateOf(TextFieldValue(viewModel?.item?.text ?: ""))
        }

        viewModel?.let { model ->
            model.textSetChangedListener = {
                inputvalue.value = TextFieldValue(viewModel?.item?.text ?: "")
            } }

        OutlinedTextField(

            value = inputvalue.value,


            onValueChange = {
                inputvalue.value = it
                 viewModel?.let {model -> model.item.text = it.text}
                            },

            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),

            placeholder = {
                Text(text = stringResource(id = R.string.hint_adding),
                    style = MaterialTheme.typography.bodyLarge)
                          },

            textStyle =  MaterialTheme.typography.bodyLarge,

            shape = RoundedCornerShape(10.dp)


        )
    }

    @Composable
    fun Priority(viewModel: AddScreenViewModel?){
        Column {

            Text(text = stringResource(id = R.string.important),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp) ,
                style = MaterialTheme.typography.bodyLarge)

            PriorityDropDownMenu(viewModel)

        }



    }

    @Composable
    fun PriorityDropDownMenu(viewModel: AddScreenViewModel?){

        var expanded by remember { mutableStateOf(false) }
        val priorityItems = stringArrayResource(id = R.array.priority).toList()
        var selectedText by remember {
            mutableStateOf(
                if(viewModel != null)
                priorityItems[Utils.getPosFromPriority(viewModel.item.priority)]
                else priorityItems[0]
            )
        }

        viewModel?.let { model ->
            model.posSetChangedListener = {
                selectedText = priorityItems[Utils.getPosFromPriority(viewModel.item.priority)]
            } }


        Box{

            Text(text = selectedText,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { expanded = !expanded },
                style = MaterialTheme.typography.bodyMedium
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                priorityItems.forEachIndexed{ind, item ->
                    DropdownMenuItem(
                        text = { Text(text = item, style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            viewModel?.let {model -> model.item.priority =
                                Utils.convertPosToPriority(ind)}
                        },
                    )
                }
            }
        }
    }

    @Composable
    fun Deadline(viewModel: AddScreenViewModel?, switchOnclickListener: (Boolean) -> Unit,
                 calendarShowListener: () -> Unit){

        val context = LocalContext.current

        var checked by remember {
            mutableStateOf(viewModel?.item?.deadline != null)
        }

        var deadline by remember {
            mutableStateOf(
                if(viewModel?.item?.deadline != null)
                    Utils.convertDateToString(viewModel.item.deadline)
                else "")
        }

        var notifyTime by remember {
            mutableStateOf(
                if(viewModel?.item?.notifyTime != null)
                    Utils.convertTimeToString(viewModel.item.notifyTime)
                else "00:00")
        }

        viewModel?.let { model ->
            model.switchSetChangedListener = {
                checked = it
            } }

        viewModel?.let { model ->
            model.dataSetChangedListener = {
            deadline = it
        } }

        viewModel?.let { model ->
            model.timeSetChangedListener = {
                notifyTime = it
            } }

        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {


                    Text(
                        text = stringResource(id = R.string.deadline),
                        modifier = Modifier
                            .padding(bottom = 8.dp, start = 16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if(checked)
                    Text(
                        text = deadline,
                        modifier = Modifier
                            .padding(bottom = 8.dp, start = 16.dp)
                            .clickable { calendarShowListener.invoke() },
                        style = MaterialTheme.typography.bodyLarge
                    )


                }

                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = !checked
                        switchOnclickListener.invoke(checked)
                    },

                    modifier = Modifier
                        .size(50.dp)
                        .padding(bottom = 8.dp, end = 32.dp),
                )
            }

            if(checked)
            Text(
                text = "Уведомить в",
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )


            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                notifyTime = Utils.convertTimeToString(cal.time)
                if(viewModel != null)
                    viewModel.item.notifyTime = cal.time
            }

            if(checked)
            Text(
                text = notifyTime,
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp)
                    .clickable {
                        TimePickerDialog(
                            context,
                            timeSetListener,
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}



