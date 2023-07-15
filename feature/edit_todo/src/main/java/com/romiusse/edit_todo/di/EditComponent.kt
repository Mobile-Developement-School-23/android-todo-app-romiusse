package com.romiusse.edit_todo.di


import android.content.Intent
import androidx.lifecycle.ViewModel
import com.romiusse.edit_todo.screen.AddScreenFragment
import com.romiusse.notifications.NotificationActions
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.utils.Feature
import dagger.Component
import kotlin.properties.Delegates

/**
 *
 * Component of edit module
 *
 * @author Romiusse
 */
@Feature
@Component(dependencies = [EditDeps::class])
interface EditComponent {
    fun inject(addScreenFragment: AddScreenFragment)

    @Component.Builder
    interface Builder{

        fun deps(listDeps: EditDeps) : Builder

        fun build(): EditComponent
    }


}

/**
 *
 * Dependencies for this module
 *
 * @author Romiusse
 */
interface EditDeps{

    val todoItemsRepository: TodoItemsRepository
    val notificationActions: NotificationActions

}

interface EditDepsProvider {

    val deps: EditDeps

    companion object : EditDepsProvider by EditDepsStore
}

object EditDepsStore : EditDepsProvider {

    override var deps: EditDeps by Delegates.notNull()
}

internal class EditComponentViewModel : ViewModel() {
    val component = DaggerEditComponent.builder().deps(EditDepsProvider.deps).build()
}



