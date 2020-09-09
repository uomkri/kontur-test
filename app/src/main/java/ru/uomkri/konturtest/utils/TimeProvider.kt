package ru.uomkri.konturtest.utils

interface TimeProvider {
    fun getCurrentTime(): Long
}

class CurrentTimeProvider() : TimeProvider {
    override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}