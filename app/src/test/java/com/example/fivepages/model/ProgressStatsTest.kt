package com.example.fivepages.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.GregorianCalendar
import java.util.TimeZone

class ProgressStatsTest {

    private val utc = TimeZone.getTimeZone("UTC")

    private fun at(year: Int, month: Int, day: Int): Long =
        GregorianCalendar(utc).apply {
            clear()
            set(year, month - 1, day, 12, 0, 0)
        }.timeInMillis

    private fun log(id: Int, bookId: Int, pages: Int, millis: Long) =
        SessionLog(id, bookId, pages, millis)

    @Test
    fun segments_roundUpSoAnyProgressShowsOneSegment() {
        assertEquals(0, progressSegments(0, 250))
        assertEquals(1, progressSegments(12, 250))
        assertEquals(1, progressSegments(25, 250))
        assertEquals(2, progressSegments(26, 250))
        assertEquals(10, progressSegments(250, 250))
        assertEquals(10, progressSegments(300, 250))
        assertEquals(0, progressSegments(10, 0))
    }

    @Test
    fun percentDone_roundsDownAndStaysInRange() {
        assertEquals(4, percentDone(12, 250))
        assertEquals(100, percentDone(250, 250))
        assertEquals(100, percentDone(300, 250))
        assertEquals(0, percentDone(5, 0))
    }

    @Test
    fun dayIndex_epochIsDayZero() {
        assertEquals(0L, dayIndex(0L, utc))
    }

    @Test
    fun streak_countsConsecutiveDaysEndingToday() {
        assertEquals(3, currentStreak(setOf(8L, 9L, 10L), today = 10L))
    }

    @Test
    fun streak_staysAliveThroughYesterday() {
        assertEquals(2, currentStreak(setOf(8L, 9L), today = 10L))
    }

    @Test
    fun streak_isZeroAfterAMissedDay() {
        assertEquals(0, currentStreak(setOf(7L, 8L), today = 10L))
    }

    @Test
    fun streak_isZeroWithNoSessions() {
        assertEquals(0, currentStreak(emptySet(), today = 10L))
    }

    @Test
    fun streak_stopsAtAGap() {
        assertEquals(1, currentStreak(setOf(8L, 10L), today = 10L))
    }

    @Test
    fun week_startsOnMondayAndSumsPagesPerDay() {
        val wednesday = at(2026, 10, 21)
        val sessions = listOf(
            log(1, 1, 5, at(2026, 10, 19)),
            log(2, 1, 3, wednesday),
            log(3, 2, 2, wednesday),
        )
        val week = weekDays(sessions, dayIndex(wednesday, utc), utc)

        assertEquals("MTWTFSS", week.joinToString("") { it.letter })
        assertEquals(listOf(5, 0, 5, 0, 0, 0, 0), week.map { it.pages })
        assertTrue(week[2].isToday)
        assertFalse(week[1].isToday)
        assertTrue(week[3].isFuture)
        assertFalse(week[2].isFuture)
    }

    @Test
    fun sessionsThisMonth_ignoresEarlierMonths() {
        val sessions = listOf(
            log(1, 1, 4, at(2026, 9, 30)),
            log(2, 1, 4, at(2026, 10, 1)),
            log(3, 1, 4, at(2026, 10, 20)),
        )
        assertEquals(2, sessionsThisMonth(sessions, at(2026, 10, 21), utc))
    }

    @Test
    fun summary_countsOnlyUnfinishedBooksAsActive() {
        val books = listOf(
            Book(1, "Atomic Habits", 200, 0L, pagesRead = 50),
            Book(2, "Deep Work", 100, 0L, pagesRead = 100),
        )
        val now = at(2026, 10, 21)
        val sessions = listOf(
            log(1, 1, 10, at(2026, 10, 20)),
            log(2, 1, 10, now),
        )
        val summary = buildProgressSummary(books, sessions, now, utc)

        assertEquals(1, summary.booksActive)
        assertEquals(2, summary.dayStreak)
        assertEquals(2, summary.sessionsThisMonth)
    }
}