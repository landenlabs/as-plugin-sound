// ----------------------------------------------------------------------
// Compile-time stub — see ProjectSystemBuildManager.kt in this directory
// for why this exists and how it's wired into the build.
// ----------------------------------------------------------------------
package com.android.tools.idea.projectsystem

import com.intellij.openapi.project.Project

class ProjectSystemService {

    fun getProjectSystem(): AndroidProjectSystem = throw NotImplementedError("stub")

    companion object {
        @JvmStatic
        fun getInstance(project: Project): ProjectSystemService = throw NotImplementedError("stub")
    }
}
