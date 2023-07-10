@file:OptIn(ExperimentalMaterial3Api::class)

package com.romiusse.edit_todo.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.romiusse.edit_todo.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class AddScreenCompose {

    @Preview
    @Composable
    fun test(closeListener: () -> Unit = {}, saveListener: () -> Unit = {},
            deleteListener: () -> Unit = {}, onPriorityChangedListener: (String) -> Unit = {},
             onSwitchStateChangedListener: () -> Unit = {}){

        AppTheme(){
            Layout(closeListener, saveListener,
            deleteListener, onPriorityChangedListener,
                onSwitchStateChangedListener)
        }

    }



    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Layout(closeListener: () -> Unit, saveListener: () -> Unit,
               deleteListener: () -> Unit, onPriorityChangedListener: (String) -> Unit,
               onSwitchStateChangedListener: () -> Unit) {

        var scrollState = rememberScrollState()

        var elevation: Dp = min(16.dp, (scrollState.value * 0.3).toInt().dp)

        Scaffold(topBar = {AppBar(elevation, closeListener, saveListener)}){
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Box(modifier = Modifier.padding(top = 64.dp)){}
                EditText()
                Priority(onPriorityChangedListener)
                Divider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))
                Deadline(onSwitchStateChangedListener)
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
    fun AppBar(elevation: Dp, closeListener: () -> Unit, saveListener: () -> Unit) {

        Surface(
            shadowElevation = elevation, // play with the elevation values
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
    fun EditText() {
        val inputvalue = remember { mutableStateOf(TextFieldValue()) }

        OutlinedTextField(
            // below line is used to get
            // value of text field,
            value = inputvalue.value,

            // below line is used to get value in text field
            // on value change in text field.
            onValueChange = { inputvalue.value = it },

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
    fun Priority(onPriorityChangedListener: (String) -> Unit){
        Column {

            Text(text = stringResource(id = R.string.important),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp) ,
                style = MaterialTheme.typography.bodyLarge)

            PriorityDropDownMenu(onPriorityChangedListener)

        }



    }

    @Composable
    fun PriorityDropDownMenu(onPriorityChangedListener: (String) -> Unit){

        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        val priorityItems = stringArrayResource(id = R.array.priority).toList()
        var selectedText by remember { mutableStateOf(priorityItems[0]) }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {

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
                priorityItems.forEach{
                    DropdownMenuItem(
                        text = { Text(text = it, style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            selectedText = it
                            expanded = false
                            onPriorityChangedListener.invoke(it)
                        },
                    )
                }
            }
        }
    }

    @Composable
    fun Deadline(onSwitchStateChangedListener: () -> Unit){

        var checked by remember { mutableStateOf(false) }

        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = stringResource(id = R.string.deadline),
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp),
                style = MaterialTheme.typography.bodyLarge)

            Switch(checked = checked,
                onCheckedChange = {checked = !checked},
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 8.dp, end = 32.dp)
                    .clickable {
                        onSwitchStateChangedListener.invoke()
                    },
            )
        }
    }
}



