package com.romiusse.edit_todo.di


import androidx.lifecycle.ViewModel
import com.romiusse.edit_todo.screen.AddScreenFragment
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.utils.Feature
import dagger.Component
import javax.inject.Singleton
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



