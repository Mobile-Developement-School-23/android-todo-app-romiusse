package com.romiusse.todoapp.dagger

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romiusse.edit_todo.di.EditDeps
import com.romiusse.server_api.api.ApiHelper
import com.romiusse.server_api.api.RetrofitBuilder
import com.romiusse.todo_list.screen.BottomSheetUtils
import com.romiusse.todoapp.App
import com.romiusse.todoapp.utils.ViewModelFactory
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.server_api.transmitter.ServerTransmitter
import com.romiusse.todo_list.di.ListDeps
import com.romiusse.todo_repository.room.AppDatabase
import com.romiusse.todo_repository.room.TodoItemDao
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(modules = [AppDataBaseModule::class, AppItemsRepositoryModule::class,
    AppBottomUtilsModule::class, AppServerModule::class])
interface AppComponent: EditDeps, ListDeps{

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

@Module
class AppDataBaseModule{


    @Singleton
    @Provides
    fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
        return appDatabase.todoItemDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(application: Application): AppDatabase {
        return AppDatabase.buildDatabase(application.applicationContext, App.DATABASE_NAME)
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}

@Module
class AppItemsRepositoryModule{

    @Singleton
    @Provides
    fun provideTodoItemsRepository(todoItemDao: TodoItemDao): TodoItemsRepository {
        return TodoItemsRepository(todoItemDao)
    }

}

@Module
class AppBottomUtilsModule{

    @Provides
    fun provideBottomUtils(): BottomSheetUtils {
        return BottomSheetUtils()
    }

}

@Module
class AppServerModule{

    @Singleton
    @Provides
    fun provideServerTransmitter(apiHelper: ApiHelper): ServerTransmitter {
        return ServerTransmitter(apiHelper)
    }

    @Singleton
    @Provides
    fun provideApiHelper(): ApiHelper {
        return ApiHelper(RetrofitBuilder.apiService)
    }

}