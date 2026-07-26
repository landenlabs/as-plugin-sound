// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.intellij.openapi.project.Project
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager

class BuildSoundListener(
    @Suppress("UNUSED_PARAMETER") project: Project
) : ProjectTaskListener {

    override fun finished(result: ProjectTaskManager.Result) {
        val state = BuildSoundSettings.getInstance().state
        if (!state.enabled) return

        val failed = result.hasErrors() || result.isAborted
        when {
            failed && state.playOnFailure   -> SoundPlayer.playFailureSound(state)
            !failed && state.playOnSuccess  -> SoundPlayer.playSuccessSound(state)
        }
    }
}
