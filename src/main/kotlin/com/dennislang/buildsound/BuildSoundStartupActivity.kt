// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.android.tools.idea.projectsystem.ProjectSystemBuildManager
import com.android.tools.idea.projectsystem.ProjectSystemService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * Registers a [ProjectSystemBuildManager.BuildListener], the API Android Studio uses
 * internally to signal build completion for every entry point (Make, Run/Debug,
 * Build APK, Rebuild). The generic platform `ProjectTaskListener` only fires for
 * JPS "Make Project" style builds, not for the Gradle invocations that back the
 * green Run button, so it stays silent on those builds.
 */
class BuildSoundStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val buildManager = ProjectSystemService.getInstance(project).getProjectSystem().getBuildManager()
        buildManager.addBuildListener(project, object : ProjectSystemBuildManager.BuildListener {
            override fun buildCompleted(result: ProjectSystemBuildManager.BuildResult) {
                val state = BuildSoundSettings.getInstance().state
                if (!state.enabled) return
                when (result.status) {
                    ProjectSystemBuildManager.BuildStatus.SUCCESS ->
                        if (state.playOnSuccess) SoundPlayer.playSuccessSound(state)
                    ProjectSystemBuildManager.BuildStatus.FAILED ->
                        if (state.playOnFailure) SoundPlayer.playFailureSound(state)
                    else -> Unit
                }
            }
        })
    }
}
