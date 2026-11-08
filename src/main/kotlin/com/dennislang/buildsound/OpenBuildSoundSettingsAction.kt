// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project

/**
 * Tools → Build Sound Settings… — jumps straight to Settings → Tools → Build Sound.
 * Also reachable from Find Action (⌘⇧A / Ctrl+Shift+A) by typing "Build Sound".
 */
class OpenBuildSoundSettingsAction : AnAction(), DumbAware {

    override fun actionPerformed(e: AnActionEvent) = open(e.project)

    companion object {
        fun open(project: Project?) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, BuildSoundConfigurable::class.java)
        }
    }
}
