package com.romiusse.todo_list.di


import androidx.lifecycle.ViewModel
import com.romiusse.server_api.transmitter.ServerTransmitter
import com.romiusse.todo_list.screen.BottomSheetUtils
import com.romiusse.todo_list.screen.MainScreenFragment
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.utils.Feature
import dagger.Component
import kotlin.properties.Delegates

/**
 *
 * Dagger component of main module
 *
 * @author Romiusse
 */
@Feature
@Component(dependencies = [ListDeps::class])
internal interface ListComponent {

    fun inject(mainScreenFragment: MainScreenFragment)

    @Component.Builder
    interface Builder{

        fun deps(listDeps: ListDeps) : Builder

        fun build(): ListComponent
    }

}

interface ListDeps{

    val todoItemsRepository: TodoItemsRepository
    val serverTransmitter: ServerTransmitter
    val bottomSheetUtils: BottomSheetUtils

}

interface ListDepsProvider {

    val deps: ListDeps

    companion object : ListDepsProvider by ListDepsStore
}

object ListDepsStore : ListDepsProvider {

    override var deps: ListDeps by Delegates.notNull()
}

internal class ListComponentViewModel : ViewModel() {
    val component = DaggerListComponent.builder().deps(ListDepsProvider.deps).build()
}

