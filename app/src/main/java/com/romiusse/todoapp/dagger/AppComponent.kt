package com.romiusse.todoapp.dagger

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romiusse.todoapp.App
import com.romiusse.todoapp.MainActivity
import com.romiusse.todoapp.screens.add.AddScreenFragment
import com.romiusse.todoapp.screens.add.AddScreenViewModel
import com.romiusse.todoapp.screens.login.LoginViewModel
import com.romiusse.todoapp.screens.main.BottomSheetUtils
import com.romiusse.todoapp.screens.main.MainScreenFragment
import com.romiusse.todoapp.screens.main.MainScreenViewModel
import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.api.RetrofitBuilder
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todoList.TodoItemsRepository
import com.romiusse.todoapp.todoList.room.TodoItemDao
import com.romiusse.todoapp.todo_list.AppDatabase
import com.romiusse.todoapp.utils.ViewModelFactory
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(modules = [AppDataBaseModule::class, AppItemsRepositoryModule::class,
    AppBottomUtilsModule::class, AppServerModule::class, ViewModelModule::class ])
interface AppComponent{
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
    fun inject(mainScreenFragment: MainScreenFragment)
    fun inject(addScreenFragment: AddScreenFragment)
}

//TODO fix this                         |
//                                      |
//                                      v
@Module
class AppDataBaseModule(private val application: Application) {


    @Singleton
    @Provides
    fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
        return appDatabase.todoItemDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context, App.DATABASE_NAME)
    }

    @Singleton
    @Provides
    fun provideContext(): Context {
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

@Module
abstract class ViewModelModule {

    @Binds
    @[IntoMap ViewModelKey(MainScreenViewModel::class)]
    internal abstract fun bindMainScreenViewModel(mainScreenViewModel: MainScreenViewModel):
            ViewModel

    @Binds
    @[IntoMap ViewModelKey(AddScreenViewModel::class)]
    internal abstract fun bindAddScreenViewModel(addScreenViewModel: AddScreenViewModel):
            ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}