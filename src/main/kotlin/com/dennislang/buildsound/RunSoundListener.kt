// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.intellij.execution.ExecutionListener
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project

/**
 * Registered project-level via plugin.xml against the platform's
 * [ExecutionListener] topic. Android Studio only attaches a process handler
 * to an Android run configuration once the APK has been installed and the
 * app process is confirmed running on the target device/emulator, so
 * [processStarted] doubles as an "install completed" signal without relying
 * on Android Studio's internal, version-fragile deploy-task APIs.
 */
class RunSoundListener(project: Project) : ExecutionListener {

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        val config = env.runProfile as? RunConfiguration ?: return
        if (config.type.id != ANDROID_RUN_CONFIGURATION_TYPE_ID) return

        val state = BuildSoundSettings.getInstance().state
        if (state.enabled && state.playOnInstall) SoundPlayer.playInstallSound(state)
    }

    companion object {
        private const val ANDROID_RUN_CONFIGURATION_TYPE_ID = "AndroidRunConfigurationType"
    }
}
