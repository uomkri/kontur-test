package ru.uomkri.konturtest.home

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.uomkri.konturtest.db.UserDao
import ru.uomkri.konturtest.net.NetUser
import ru.uomkri.konturtest.repo.UsersRepository
import ru.uomkri.konturtest.utils.TimeProvider
import java.io.IOException

class HomeViewModel(
    private val database: UserDao,
    application: Application,
    private val timeProvider: TimeProvider
) : AndroidViewModel(application) {

    private val usersRepository = UsersRepository(database)

    val users = usersRepository.users

    private val _hasRequestFailed = MutableLiveData<Boolean>()
    val hasRequestFailed: LiveData<Boolean>
        get() = _hasRequestFailed

    private val _shouldProgressBeShown = MutableLiveData<Boolean>()
    val shouldProgressBeShown: LiveData<Boolean>
        get() = _shouldProgressBeShown

    fun refreshDataFromRepository(activity: Activity) {
        saveCurrentTime(activity)
        viewModelScope.launch {
            try {
                _shouldProgressBeShown.value = true
                usersRepository.refreshUsers()
                _hasRequestFailed.value = false
                _shouldProgressBeShown.value = false
            } catch (e: IOException) {
                _hasRequestFailed.value = true
            }
        }
    }

    fun isUpdateNeeded(lastUpdate: Long): Boolean {
        val timeSinceLastUpdate = timeSinceLastUpdate(lastUpdate)
        return timeSinceLastUpdate > 60.0
    }

    private fun timeSinceLastUpdate(lastUpdate: Long): Float {
        val currentTime = timeProvider.getCurrentTime()
        val delta = currentTime - lastUpdate
        return delta / 1000.0f
    }

    private fun saveCurrentTime(activity: Activity) {
        val prefs = activity.getSharedPreferences("time", Context.MODE_PRIVATE)
        prefs.edit()
            .putLong("lastUpdate", timeProvider.getCurrentTime())
            .apply()
    }

    fun resetRequestStatus() {
        _hasRequestFailed.value = null
    }

    fun filterByName(name: String): List<NetUser> {
        return users.value!!.filter {
            it.name.contains(name)
        }
    }

    fun filterByPhone(phone: String): List<NetUser> {
        return users.value!!.filter {
            it.phone.contains(phone)
        }
    }

}