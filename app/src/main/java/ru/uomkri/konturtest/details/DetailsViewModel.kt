package ru.uomkri.konturtest.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.uomkri.konturtest.db.UserDao
import ru.uomkri.konturtest.net.UserEducationPeriod
import ru.uomkri.konturtest.repo.UsersRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DetailsViewModel(
    private val database: UserDao,
    application: Application
) : AndroidViewModel(application) {

    private val usersRepository = UsersRepository(database)

    val selectedUser = usersRepository.selectedUser

    fun getUserById(userId: String) {
        viewModelScope.launch {
            usersRepository.getUserById(userId)
        }
    }

    fun renderEducationPeriod(period: UserEducationPeriod): String {
        val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz")
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val startParsed = ZonedDateTime.parse(period.start, parser).format(formatter)
        val endParsed = ZonedDateTime.parse(period.end, parser).format(formatter)

        return "$startParsed - $endParsed"
    }
}