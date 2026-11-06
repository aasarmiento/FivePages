package com.example.fivepages.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PagesInputTest {

    @Test
    fun pagesToLog_readsATypedNumber() {
        assertEquals(25, pagesToLog("25", 100))
    }

    @Test
    fun pagesToLog_emptyOrNoDigitsIsZero() {
        assertEquals(0, pagesToLog("", 100))
        assertEquals(0, pagesToLog("abc", 100))
    }

    @Test
    fun pagesToLog_dropsLettersAndSpaces() {
        assertEquals(12, pagesToLog(" 1a2 ", 100))
    }

    @Test
    fun pagesToLog_isCappedAtThePagesLeft() {
        assertEquals(40, pagesToLog("500", 40))
        assertEquals(0, pagesToLog("5", 0))
    }
}
