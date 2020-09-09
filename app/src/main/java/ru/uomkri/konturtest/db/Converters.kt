package ru.uomkri.konturtest.db

import androidx.room.TypeConverter
import ru.uomkri.konturtest.net.UserEducationPeriod

class Converters {
    @TypeConverter
    fun fromUserEducationPeriod(period: UserEducationPeriod): String {
        return "${period.start},${period.end}"
    }

    @TypeConverter
    fun toUserEducationPeriod(data: String): UserEducationPeriod {
        val list = data.split(",")
        return UserEducationPeriod(list[0], list[1])
    }
}