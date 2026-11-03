package com.squarestudios.simplyseek.ui.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

/**
 * Interface defining haptic event actions during word search gameplay.
 */
interface HapticFeedbackController {
    /**
     * Subtle, snappy tactile tick emitted whenever the user's drag path spans across a new tile.
     */
    fun performTileTick()

    /**
     * Tactile tap emitted when the player first touches down on a start tile.
     */
    fun performSelectionStart()

    /**
     * Rewarding, distinct haptic confirmation emitted upon successfully finding a target word.
     */
    fun performWordFoundSuccess()
}

/**
 * Controller providing tactile haptic feedback for game interactions in SimplySeek.
 *
 * Utilizes standard Android [View.performHapticFeedback] with fallback to [Vibrator]
 * to ensure crisp, satisfying physical tactile sensations across devices and API levels.
 */
class GameHapticFeedback(
    private val view: View?,
    private val vibrator: Vibrator?,
) : HapticFeedbackController {
    private var lastTickTimestampMs: Long = 0L

    override fun performTileTick() {
        val now = System.currentTimeMillis()
        if ((now - lastTickTimestampMs) < 35L) return
        lastTickTimestampMs = now

        try {
            view?.isHapticFeedbackEnabled = true
            val constant = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> HapticFeedbackConstants.SEGMENT_TICK
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> HapticFeedbackConstants.TEXT_HANDLE_MOVE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> HapticFeedbackConstants.TEXT_HANDLE_MOVE
                else -> HapticFeedbackConstants.CLOCK_TICK
            }
            val performed = view?.performHapticFeedback(constant, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING) ?: false
            if (!performed && vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10L, 50))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(10L)
                }
            }
        } catch (_: Throwable) {
            // Gracefully ignore devices with restricted or absent haptic capabilities
        }
    }

    override fun performSelectionStart() {
        try {
            view?.isHapticFeedbackEnabled = true
            val constant = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> HapticFeedbackConstants.GESTURE_START
                else -> HapticFeedbackConstants.KEYBOARD_TAP
            }
            val performed = view?.performHapticFeedback(constant, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING) ?: false
            if (!performed && vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(15L, 80))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(15L)
                }
            }
        } catch (_: Throwable) {
        }
    }

    override fun performWordFoundSuccess() {
        try {
            view?.isHapticFeedbackEnabled = true
            val constant = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> HapticFeedbackConstants.CONFIRM
                else -> HapticFeedbackConstants.KEYBOARD_TAP
            }
            val performed = view?.performHapticFeedback(constant, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING) ?: false
            if (!performed && vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(0, 20, 40, 30),
                            intArrayOf(0, 150, 0, 220),
                            -1,
                        ),
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 20, 40, 30), -1)
                }
            }
        } catch (_: Throwable) {
        }
    }
}

/**
 * No-op implementation of [HapticFeedbackController] for testing or when haptics are disabled.
 */
object NoOpHapticFeedbackController : HapticFeedbackController {
    override fun performTileTick() {}
    override fun performSelectionStart() {}
    override fun performWordFoundSuccess() {}
}

/**
 * Creates and remembers a [GameHapticFeedback] instance bound to the current View and Context.
 */
@Composable
fun rememberGameHapticFeedback(): GameHapticFeedback {
    val view = LocalView.current
    val context = LocalContext.current
    return remember(view, context) {
        val vibrator = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Throwable) {
            null
        }
        GameHapticFeedback(view, vibrator)
    }
}
