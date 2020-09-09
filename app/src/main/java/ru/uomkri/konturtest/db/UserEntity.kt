package ru.uomkri.konturtest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.uomkri.konturtest.net.NetUser
import ru.uomkri.konturtest.net.UserEducationPeriod

@Entity(tableName = "users")
data class DBUser(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "biography") val biography: String,
    @ColumnInfo(name = "temperament") val temperament: String,
    @ColumnInfo(name = "educationPeriod") val educationPeriod: UserEducationPeriod,
    @ColumnInfo(name = "phone") val phone: String
)

fun List<DBUser>.asDomainModel(): List<NetUser> {
    return map {
        NetUser(
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