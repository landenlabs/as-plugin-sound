// ----------------------------------------------------------------------
// Copyright (c) 2026 LanDen Labs - Dennis Lang
// https://landenlabs.com
// ----------------------------------------------------------------------
package com.dennislang.buildsound

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project

/**
 * Shows a one-time balloon after the plugin is first installed or updated,
 * with an "Open Settings" link to Settings → Tools → Build Sound.
 */
object WelcomeNotifier {

    private const val PLUGIN_ID = "com.dennislang.build-sound"    // must match <id> in plugin.xml
    private const val GROUP_ID  = "Build Sound"                   // must match <notificationGroup id> in plugin.xml

    fun showIfNewVersion(project: Project) {
        val plugin  = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID)) ?: return
        val version = plugin.version ?: return
        val state   = BuildSoundSettings.getInstance().state

        // Several projects can open at startup; only the first one claims the notification.
        val previous = synchronized(this) {
            val prev = state.lastNotifiedVersion
            if (prev == version) return
            state.lastNotifiedVersion = version
            prev
        }

        val title = if (previous.isEmpty()) "${plugin.name} installed" else "${plugin.name} updated to $version"
        NotificationGroupManager.getInstance()
            .getNotificationGroup(GROUP_ID)
            .createNotification(
                title,
                "Plays a tone when builds finish and when the app installs on a device. " +
                    "Settings: Tools → Build Sound Settings…",
                NotificationType.INFORMATION
            )
            .addAction(NotificationAction.createSimpleExpiring("Open Settings") {
                OpenBuildSoundSettingsAction.open(project)
            })
            .notify(project)
    }
}
