// ----------------------------------------------------------------------
// Compile-time stub — see ProjectSystemBuildManager.kt in this directory
// for why this exists and how it's wired into the build.
// ----------------------------------------------------------------------
package com.android.tools.idea.projectsystem

interface AndroidProjectSystem {
    fun getBuildManager(): ProjectSystemBuildManager
}
