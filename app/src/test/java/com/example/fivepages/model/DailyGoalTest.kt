package com.example.fivepages.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DailyGoalTest {

    @Test
    fun dailyGoal_splitsPagesEvenlyAcrossDays() {
        assertEquals(10, dailyGoal(100, 10))
        assertEquals(20, dailyGoal(100, 5))
    }

    @Test
    fun dailyGoal_onTheLastDayIsAllThePagesLeft() {
        assertEquals(30, dailyGoal(30, 1))
    }
}