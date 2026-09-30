package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class DayItem(
    val dateString: String,
    val dayOfWeek: String,
    val dayAndMonth: String,
    val dayNumber: String,
    val isToday: Boolean,
    val isStudied: Boolean
)

object DateUtils {
    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val SPANISH_LOCALE = Locale("es", "ES")

    fun getTodayDateString(): String {
        return DATE_FORMAT.format(Date())
    }

    fun getDateStringForDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return DATE_FORMAT.format(calendar.time)
    }

    fun parseDate(dateString: String): Date? {
        return try {
            DATE_FORMAT.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun getDayOfWeekShort(dateString: String): String {
        return try {
            val date = DATE_FORMAT.parse(dateString) ?: return ""
            val dayFormat = SimpleDateFormat("EEE", SPANISH_LOCALE)
            dayFormat.format(date).replace(".", "").uppercase()
        } catch (e: Exception) {
            ""
        }
    }

    fun getDayNumber(dateString: String): String {
        return try {
            val date = DATE_FORMAT.parse(dateString) ?: return ""
            val numFormat = SimpleDateFormat("d", SPANISH_LOCALE)
            numFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun getFormattedDayMonth(dateString: String): String {
        return try {
            val date = DATE_FORMAT.parse(dateString) ?: return ""
            val monthFormat = SimpleDateFormat("d 'de' MMM", SPANISH_LOCALE)
            monthFormat.format(date).replace(".", "")
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * Calcula la racha actual de días consecutivos.
     * Si hoy está estudiado, cuenta hacia atrás desde hoy.
     * Si hoy todavía no está estudiado pero ayer sí, la racha está "viva" esperando a hoy.
     * Si ni hoy ni ayer se estudió, la racha es 0.
     */
    fun calculateCurrentStreak(studiedDates: Set<String>): Int {
        if (studiedDates.isEmpty()) return 0

        val today = getTodayDateString()
        val yesterday = getDateStringForDaysAgo(1)

        val studiedToday = studiedDates.contains(today)
        val studiedYesterday = studiedDates.contains(yesterday)

        if (!studiedToday && !studiedYesterday) {
            return 0
        }

        var streak = 0
        var daysAgo = if (studiedToday) 0 else 1

        while (true) {
            val targetDate = getDateStringForDaysAgo(daysAgo)
            if (studiedDates.contains(targetDate)) {
                streak++
                daysAgo++
            } else {
                break
            }
        }

        return streak
    }

    /**
     * Calcula la mejor racha histórica de días consecutivos.
     */
    fun calculateBestStreak(studiedDates: Set<String>): Int {
        if (studiedDates.isEmpty()) return 0
        val sortedDates = studiedDates.mapNotNull { parseDate(it) }.sorted()
        if (sortedDates.isEmpty()) return 0

        var maxStreak = 1
        var currentStreak = 1

        for (i in 1 until sortedDates.size) {
            val prev = Calendar.getInstance().apply { time = sortedDates[i - 1] }
            val curr = Calendar.getInstance().apply { time = sortedDates[i] }

            // Normalizar a medianoche
            prev.set(Calendar.HOUR_OF_DAY, 0)
            prev.set(Calendar.MINUTE, 0)
            prev.set(Calendar.SECOND, 0)
            prev.set(Calendar.MILLISECOND, 0)

            curr.set(Calendar.HOUR_OF_DAY, 0)
            curr.set(Calendar.MINUTE, 0)
            curr.set(Calendar.SECOND, 0)
            curr.set(Calendar.MILLISECOND, 0)

            val diffMillis = curr.timeInMillis - prev.timeInMillis
            val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

            if (diffDays == 1L) {
                currentStreak++
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                }
            } else if (diffDays > 1L) {
                currentStreak = 1
            }
        }

        return maxStreak
    }

    /**
     * Obtiene los últimos 7 días terminando en hoy.
     * Ordenados cronológicamente (desde hace 6 días hasta hoy).
     */
    fun getLast7Days(studiedDates: Set<String>): List<DayItem> {
        val today = getTodayDateString()
        val list = mutableListOf<DayItem>()

        // 6 días atrás hasta 0 (hoy)
        for (i in 6 downTo 0) {
            val dateStr = getDateStringForDaysAgo(i)
            list.add(
                DayItem(
                    dateString = dateStr,
                    dayOfWeek = getDayOfWeekShort(dateStr),
                    dayAndMonth = getFormattedDayMonth(dateStr),
                    dayNumber = getDayNumber(dateStr),
                    isToday = (dateStr == today),
                    isStudied = studiedDates.contains(dateStr)
                )
            )
        }
        return list
    }
}
