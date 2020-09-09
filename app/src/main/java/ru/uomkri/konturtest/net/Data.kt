package ru.uomkri.konturtest.net

import ru.uomkri.konturtest.db.DBUser

data class NetUser(
    val id: String,
    val name: String,
    val phone: String,
    val height: Float,
    val biography: String,
    val temperament: String,
    val educationPeriod: UserEducationPeriod
)

data class UserEducationPeriod(
    val start: String,
    val end: String
)

fun List<NetUser>.asDatabaseModel(): List<DBUser> {
    return map {
        DBUser(
            id = it.id,
            phone = it.phone,
            name = it.name,
            height = it.height,
            biography = it.biography,
            temperament = it.temperament,
            educationPeriod = it.educationPeriod
        )
    }
}
