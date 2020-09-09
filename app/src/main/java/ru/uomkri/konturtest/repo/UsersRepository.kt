package ru.uomkri.konturtest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import ru.uomkri.konturtest.db.*
import ru.uomkri.konturtest.net.UserResource
import ru.uomkri.konturtest.net.NetUser
import ru.uomkri.konturtest.net.asDatabaseModel

class UsersRepository(private val database: UserDao) {

    suspend fun refreshUsers() {

        withContext(Dispatchers.IO) {

            val sourceList = listOf(1, 2, 3)

            val requests = sourceList.map {
                UserResource.retrofitService.getUsersFromSource(it)
            }

            val results = requests.awaitAll().flatten()

            database.insertAll(results.asDatabaseModel())
        }
    }

    suspend fun getUserById(userId: String) {
        withContext(Dispatchers.IO) {
            val user = database.getUserById(userId)
            _selectedUser.postValue(user)
        }
    }

    private val _selectedUser = MutableLiveData<DBUser>()
    val selectedUser: LiveData<DBUser>
        get() = _selectedUser

    val users: LiveData<List<NetUser>> = Transformations.map(database.getAllUsers()) {
        it.asDomainModel()
    }
}