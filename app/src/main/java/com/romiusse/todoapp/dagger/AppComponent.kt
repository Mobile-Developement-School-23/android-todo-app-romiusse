package com.romiusse.todoapp.dagger

import android.app.Application
import android.content.Context
import com.romiusse.edit_todo.di.EditDeps
import com.romiusse.server_api.api.ApiHelper
import com.romiusse.server_api.api.RetrofitBuilder
import com.romiusse.todo_list.screen.BottomSheetUtils
import com.romiusse.todoapp.App
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.server_api.transmitter.ServerTransmitter
import com.romiusse.todo_list.di.ListDeps
import com.romiusse.todo_repository.room.AppDatabase
import com.romiusse.todo_repository.room.TodoItemDao
import com.romiusse.todoapp.MainActivityWorkManager
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

/**
 * A root of *modules*.
 *
 * This interface is the main root of dagger
 *
 * @author Romiusse
 */
@AppScope
@Component(modules = [AppDataBaseModule::class, AppItemsRepositoryModule::class,
    AppBottomUtilsModule::class, AppServerModule::class])
interface AppComponent: EditDeps, ListDeps{
    fun inject(mainActivityWorkManager: MainActivityWorkManager)

    override val bottomSheetUtils: BottomSheetUtils
    override val serverTransmitter: ServerTransmitter
    override val todoItemsRepository: TodoItemsRepository

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application) : Builder

        fun build() : AppComponent

    }
}

/**
 * This dagger module provides data base classes
 *
 * @author Romiusse
 */
@Module
internal class AppDataBaseModule{


    @AppScope
    @Provides
    fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
        return appDatabase.todoItemDao()
    }

    @AppScope
    @Provides
    fun provideDataBase(application: Application): AppDatabase {
        return AppDatabase.buildDatabase(application.applicationContext, App.DATABASE_NAME)
    }

    @AppScope
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}

/**
 * This dagger module provides app repository
 *
 * @author Romiusse
 */
@Module
internal class AppItemsRepositoryModule{

    @AppScope
    @Provides
    fun provideTodoItemsRepository(todoItemDao: TodoItemDao): TodoItemsRepository {
        return TodoItemsRepository(todoItemDao)
    }

}

/**
 * This dagger module provides bottom utils
 *
 * @author Romiusse
 */
@Module
internal class AppBottomUtilsModule{

    @Provides
    fun provideBottomUtils(): BottomSheetUtils {
        return BottomSheetUtils()
    }

}

/**
 * This dagger module provides server api classes
 *
 * @author Romiusse
 */
@Module
internal class AppServerModule{

    @AppScope
    @Provides
    fun provideServerTransmitter(apiHelper: ApiHelper): ServerTransmitter {
        return ServerTransmitter(apiHelper)
    }

    @AppScope
    @Provides
    fun provideApiHelper(): ApiHelper {
        return ApiHelper(RetrofitBuilder.apiService)
    }

}

/**
 * Custom scope
 *
 * @author Romiusse
 */
@Scope
internal annotation class AppScope