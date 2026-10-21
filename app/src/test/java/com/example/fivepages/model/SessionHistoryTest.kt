package com.example.fivepages.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.TimeZone

class SessionHistoryTest {

    @Test
    fun duration_zeroShowsDash() {
        assertEquals("—", formatDuration(0))
    }

    @Test
    fun duration_underAMinuteRoundsToOneMinute() {
        assertEquals("1m", formatDuration(20))
    }

    @Test
    fun duration_minutesOnly() {
        assertEquals("5m", formatDuration(300))
    }

    @Test
    fun duration_hoursAndMinutes() {
        assertEquals("1h 10m", formatDuration(4200))
    }

    @Test
    fun duration_wholeHours() {
        assertEquals("2h", formatDuration(7200))
    }

    @Test
    fun pages_singularAndPlural() {
        assertEquals("1 page", pagesLabel(1))
        assertEquals("5 pages", pagesLabel(5))
    }

    @Test
    fun date_usesMonthDayYear() {
        val utc = TimeZone.getTimeZone("UTC")
        assertEquals("09/19/26", formatSessionDate(1_789_819_200_000L, utc))
    }

    @Test
    fun history_onlyThisBookNewestFirst() {
        val logs = listOf(
            SessionLog(1, 1, 3, 1_000L),
            SessionLog(2, 2, 4, 5_000L),
            SessionLog(3, 1, 2, 9_000L),
            SessionLog(4, 1, 6, 9_000L),
        )
        assertEquals(listOf(4, 3, 1), sessionsForBook(logs, 1).map { it.id })
    }
}