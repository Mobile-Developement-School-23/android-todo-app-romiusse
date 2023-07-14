package com.romiusse.edit_todo.screen

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.romiusse.edit_todo.R
import com.romiusse.edit_todo.compose.AddScreenCompose
import com.romiusse.edit_todo.di.EditComponentViewModel
import com.romiusse.notifications.Notifications
import com.romiusse.notifications.channelID
import com.romiusse.notifications.messageExtra
import com.romiusse.notifications.titleExtra
import com.romiusse.utils.Utils
import dagger.Lazy
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

/**
 *
 * Fragment of add screen
 *
 * @author Romiusse
 */
class AddScreenFragment : Fragment() {

    @Inject
    internal lateinit var addScreenViewModelFactory: Lazy<AddScreenViewModel.Factory>

    private val viewModel: AddScreenViewModel by viewModels {
        addScreenViewModelFactory.get()
    }

    private var deadline: Date? = null
        set(value) {
            field = value
            viewModel.updateDeadline(value)
        }
    private var materialDatePicker = MaterialDatePicker.Builder.datePicker().build()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<EditComponentViewModel>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val arguments = arguments
        if(arguments != null)
            arguments.getString("id")?.let { viewModel.loadItem(id = it) }
        else
            viewModel.loadItem()

        return ComposeView(requireContext()).apply {
            setContent {
                AddScreenCompose().test(
                                        closeListener = {closeButtonOnClickListener()},
                                        saveListener = {saveButtonOnClickListener()},
                                        deleteListener = {deleteButtonOnClickListener()},
                                        switchOnclickListener = {switchOnclickListener(it)},
                                        calendarShowListener = {calendarListener()},
                                        viewModel = viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCalendar()
        createNotificationChannel()

    }

    private fun createNotificationChannel()
    {
        val name = "TodoApp"
        val desc = "Уведомления о дедлайнах"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE)
                    as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun saveButtonOnClickListener(){
        if(viewModel.item.text != "") {
            if(arguments != null) updateItem()
            else createItem()
            close()
        }
        else Toast.makeText(context, getString(R.string.null_text), Toast.LENGTH_SHORT).show()
    }

    private fun closeButtonOnClickListener(){
        close()
    }
    private fun deleteButtonOnClickListener(){
        deleteItem()
        close()
    }

    private fun switchOnclickListener(isChecked: Boolean){
        if(isChecked){
            materialDatePicker.show(parentFragmentManager, "tag")
            if(deadline == null) deadline = Date()
            viewModel.dataSetChangedListener.invoke(Utils.convertDateToString(deadline))
            val cal = Calendar.getInstance()
            cal.set(0, 0, 0, 0, 0, 0)
            viewModel.item.notifyTime = cal.time
        }
        else{
            deadline = null
            viewModel.dataSetChangedListener.invoke("")
        }
    }

    private fun calendarListener(){
        materialDatePicker.show(parentFragmentManager, "tag")
    }


    private fun initCalendar() {
        materialDatePicker.addOnPositiveButtonClickListener {
            deadline = Date(it)
            viewModel.dataSetChangedListener.invoke(Utils.convertDateToString(deadline))
        }

    }

    private fun createItem(){
        createNotification()
        viewModel.createItem()
    }

    private fun deleteItem(){
        removeNotification()
        viewModel.deleteItem()
    }

    private fun updateItem(){
        removeNotification()
        createNotification()
        viewModel.updateItem()
    }

    private fun close(){

        findNavController().navigateUp()
    }

    private fun createNotification(){

        if(viewModel.item.deadline == null || viewModel.item.notifyTime == null) return

        val cal = Calendar.getInstance()
        val dateCal = Calendar.getInstance()
        val timeCal = GregorianCalendar.getInstance()

        dateCal.time = viewModel.item.deadline!!
        timeCal.time = viewModel.item.notifyTime!!

        cal.set(dateCal.get(Calendar.YEAR), dateCal.get(Calendar.MONTH), dateCal.get(Calendar.DATE),
            timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE), 0)

        val d = cal.time

        scheduleNotification(
            (viewModel.item.id.toLong() % Int.MAX_VALUE).toInt(),
            d
        )
    }

    private fun removeNotification(){
        removeScheduleNotification((viewModel.item.id.toLong() % Int.MAX_VALUE).toInt())
    }

    private fun removeScheduleNotification(code: Int){
        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), Notifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            code,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
    private fun scheduleNotification(code: Int, notifyTime: Date)
    {
        val intent = Intent(requireContext(), Notifications::class.java)
        intent.putExtra(titleExtra, "Осталось мало времени")
        intent.putExtra(messageExtra, "Поспешите, уже сегодня закончится срок выполнения вашей задачи")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            code,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = notifyTime.time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }


}
