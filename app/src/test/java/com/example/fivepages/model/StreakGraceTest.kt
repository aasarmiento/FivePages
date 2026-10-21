package com.example.fivepages.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StreakGraceTest {

    private val today = 20_000L

    private fun days(vararg offsets: Int): Set<Long> = offsets.map { today + it }.toSet()

    @Test
    fun missed_isZeroWhenYesterdayWasRead() {
        assertEquals(0, missedYesterdayStreak(days(-1, -2, -3), emptySet(), today))
    }

    @Test
    fun missed_isZeroWhenTodayAlreadyHasASession() {
        assertEquals(0, missedYesterdayStreak(days(0, -3, -4), emptySet(), today))
    }

    @Test
    fun missed_returnsTheStreakThatCanBeSaved() {
        assertEquals(3, missedYesterdayStreak(days(-2, -3, -4), emptySet(), today))
    }

    @Test
    fun missed_isZeroWhenThereWasNoStreakToSave() {
        assertEquals(0, missedYesterdayStreak(days(-5), emptySet(), today))
    }

    @Test
    fun missed_isZeroOnceYesterdayIsFrozen() {
        assertEquals(0, missedYesterdayStreak(days(-2, -3), days(-1), today))
    }

    @Test
    fun freeze_keepsTheStreakAliveWithoutCountingTheFrozenDay() {
        assertEquals(2, streakWithFreezes(days(-2, -3), days(-1), today))
    }

    @Test
    fun freeze_streakContinuesWhenTodayIsRead() {
        assertEquals(3, streakWithFreezes(days(0, -2, -3), days(-1), today))
    }

    @Test
    fun streak_isZeroAfterAMissedDayWithoutFreeze() {
        assertEquals(0, streakWithFreezes(days(-2, -3), emptySet(), today))
    }

    @Test
    fun freezesLeft_startsAtOneAndDropsToZeroAfterUse() {
        assertEquals(1, freezesLeft(emptySet(), today))
        assertEquals(0, freezesLeft(days(-1), today))
    }

    @Test
    fun freezesLeft_refillsInANewMonth() {
        val lastMonth = today - 40
        assertEquals(1, freezesLeft(setOf(lastMonth), today))
    }

    @Test
    fun monthKey_changesOnTheFirstOfTheMonth() {
        val lastDayOfSeptember = 20_726L
        assertEquals(monthKey(lastDayOfSeptember) + 1, monthKey(lastDayOfSeptember + 1))
    }
}