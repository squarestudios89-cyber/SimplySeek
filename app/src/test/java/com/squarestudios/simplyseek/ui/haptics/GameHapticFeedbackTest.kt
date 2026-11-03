package com.squarestudios.simplyseek.ui.haptics

import org.junit.Assert.assertEquals
import org.junit.Test

class GameHapticFeedbackTest {

    @Test
    fun testNoOpHapticFeedbackController() {
        // Assert that calling NoOp controller methods does not crash
        NoOpHapticFeedbackController.performTileTick()
        NoOpHapticFeedbackController.performSelectionStart()
        NoOpHapticFeedbackController.performWordFoundSuccess()
    }

    @Test
    fun testGameHapticFeedbackWithNullsDoesNotCrash() {
        val haptics = GameHapticFeedback(view = null, vibrator = null)
        haptics.performSelectionStart()
        haptics.performTileTick()
        haptics.performWordFoundSuccess()
    }

    @Test
    fun testRecordingHapticFeedbackController() {
        val events = mutableListOf<String>()
        val testController = object : HapticFeedbackController {
            override fun performTileTick() {
                events.add("TICK")
            }

            override fun performSelectionStart() {
                events.add("START")
            }

            override fun performWordFoundSuccess() {
                events.add("SUCCESS")
            }
        }

        testController.performSelectionStart()
        testController.performTileTick()
        testController.performTileTick()
        testController.performWordFoundSuccess()

        assertEquals(4, events.size)
        assertEquals("START", events[0])
        assertEquals("TICK", events[1])
        assertEquals("TICK", events[2])
        assertEquals("SUCCESS", events[3])
    }
}
