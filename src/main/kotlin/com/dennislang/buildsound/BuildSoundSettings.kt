// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "BuildSoundSettings",
    storages = [Storage("build-sound.xml")]
)
class BuildSoundSettings : PersistentStateComponent<BuildSoundSettings.State> {

    data class State(
        var enabled: Boolean = true,
        var playOnSuccess: Boolean = true,
        var playOnFailure: Boolean = true,
        // Frequencies in Hz
        var successHighFreq: Int = 880,
        var successLowFreq: Int = 659,
        var failureHighFreq: Int = 440,
        var failureLowFreq: Int = 294,
        // Volume 0..100
        var volume: Int = 70,
        // Tone duration in ms
        var toneDurationMs: Int = 180
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): BuildSoundSettings =
            ApplicationManager.getApplication().getService(BuildSoundSettings::class.java)
    }
}
