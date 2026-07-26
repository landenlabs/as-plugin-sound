// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import javax.sound.sampled.*
import java.io.ByteArrayInputStream
import kotlin.math.*

object SoundPlayer {

    private const val SAMPLE_RATE = 44100f
    private const val FADE_SAMPLES = 512   // fade in/out to avoid clicks

    /**
     * Plays a sequence of (frequency Hz, duration ms) tones in a daemon thread.
     * [volume] is 0..100.
     */
    fun playSequence(tones: List<Pair<Int, Int>>, volume: Int) {
        val amplitude = volume.coerceIn(0, 100) / 100.0
        Thread {
            for ((freq, ms) in tones) {
                playTone(freq, ms, amplitude)
            }
        }.apply { isDaemon = true }.start()
    }

    fun playSuccessSound(s: BuildSoundSettings.State) =
        playSequence(
            listOf(s.successLowFreq to s.toneDurationMs, s.successHighFreq to (s.toneDurationMs + 60)),
            s.volume
        )

    fun playFailureSound(s: BuildSoundSettings.State) =
        playSequence(
            listOf(s.failureHighFreq to s.toneDurationMs, s.failureLowFreq to (s.toneDurationMs + 60)),
            s.volume
        )

    private fun playTone(frequency: Int, durationMs: Int, amplitude: Double) {
        try {
            val numSamples = (SAMPLE_RATE * durationMs / 1000).toInt()
            val data = ByteArray(numSamples * 2)   // 16-bit mono LE

            for (i in 0 until numSamples) {
                val envelope = when {
                    i < FADE_SAMPLES -> i.toDouble() / FADE_SAMPLES
                    i > numSamples - FADE_SAMPLES -> (numSamples - i).toDouble() / FADE_SAMPLES
                    else -> 1.0
                }
                val sample = sin(2.0 * PI * i * frequency / SAMPLE_RATE) * amplitude * envelope
                val value = (sample * Short.MAX_VALUE).toInt().toShort()
                data[i * 2]     = (value.toInt() and 0xFF).toByte()
                data[i * 2 + 1] = ((value.toInt() ushr 8) and 0xFF).toByte()
            }

            val format = AudioFormat(SAMPLE_RATE, 16, 1, true, false)
            val clip = AudioSystem.getClip()
            clip.open(AudioInputStream(ByteArrayInputStream(data), format, numSamples.toLong()))
            clip.addLineListener { event ->
                if (event.type == LineEvent.Type.STOP) clip.close()
            }
            clip.start()

            // Wait for this tone to finish before playing the next
            Thread.sleep(durationMs.toLong() + 20)
        } catch (_: Exception) {
            // Silently swallow — audio device unavailable or line busy
        }
    }
}
