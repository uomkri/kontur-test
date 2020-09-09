package ru.uomkri.konturtest.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.uomkri.konturtest.db.UserDao
import ru.uomkri.konturtest.utils.TimeProvider
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
    private val dataSource: UserDao,
    private val application: Application,
    private val timeProvider: TimeProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, application, timeProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}