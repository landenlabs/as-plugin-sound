// ----------------------------------------------------------------------
// Compile-time stub. Mirrors the real com.android.tools.idea.projectsystem
// API shipped inside Android Studio's bundled "org.jetbrains.android"
// plugin (declared as a runtime <depends> in plugin.xml). This source set
// is compileOnly and never packaged into the plugin jar — at runtime the
// real class from Android Studio's own classloader is used instead, so
// method names/signatures below must match the real API exactly.
// ----------------------------------------------------------------------
package com.android.tools.idea.projectsystem

import com.intellij.openapi.Disposable

interface ProjectSystemBuildManager {

    enum class BuildMode { UNKNOWN, COMPILE_OR_ASSEMBLE, CLEAN }
    enum class BuildStatus { UNKNOWN, SUCCESS, FAILED, CANCELLED }
    class BuildResult(val mode: BuildMode, val status: BuildStatus)

    interface BuildListener {
        fun buildStarted(mode: BuildMode) {}
        fun beforeBuildCompleted(result: BuildResult) {}
        fun buildCompleted(result: BuildResult) {}
    }

    fun compileProject()
    fun getLastBuildResult(): BuildResult
    fun addBuildListener(parentDisposable: Disposable, listener: BuildListener)
    fun isBuilding(): Boolean
}
