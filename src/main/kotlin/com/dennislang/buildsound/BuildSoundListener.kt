package com.dennislang.buildsound

import com.intellij.openapi.project.Project
import com.intellij.task.ProjectTaskContext
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskRunner

class BuildSoundListener(
    @Suppress("UNUSED_PARAMETER") project: Project
) : ProjectTaskListener {

    override fun finished(context: ProjectTaskContext, executionResult: ProjectTaskRunner.Result) {
        val state = BuildSoundSettings.getInstance().state
        if (!state.enabled) return

        val failed = executionResult.hasErrors() || executionResult.isAborted
        when {
            failed && state.playOnFailure   -> SoundPlayer.playFailureSound(state)
            !failed && state.playOnSuccess  -> SoundPlayer.playSuccessSound(state)
        }
    }
}
