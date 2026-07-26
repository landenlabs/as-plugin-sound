plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group   = "com.dennislang"
// Single source of truth is the VERSION file at repo root; set-version.bash /
// set-version.ps1 rewrite it (and README's <!-- VERSION --> marker) together.
version = file("VERSION").readText().trim()

repositories {
    mavenCentral()
}

// Compile-time-only stubs for com.android.tools.idea.projectsystem.* (see
// stubs/kotlin). Android Studio's own "org.jetbrains.android" plugin supplies
// the real classes at runtime (declared as a <depends> in plugin.xml); these
// sources share the main compile classpath but are excluded from the built
// jar below, so they never ship (and can't shadow the real classes at runtime).
sourceSets {
    main {
        kotlin.srcDir("stubs/kotlin")
    }
}

tasks.jar {
    exclude("com/android/**")
}

intellij {
    version.set("2024.1")
    type.set("IC")          // IntelliJ Community; switch to "AI" to sandbox inside Android Studio
    plugins.set(listOf())
    updateSinceUntilBuild.set(false)
}

kotlin {
    jvmToolchain(17)
    // Prevent the Kotlin stdlib from being bundled — IntelliJ Platform ships its own.
    compilerOptions {
        freeCompilerArgs.add("-Xno-stdlib")
    }
}

tasks {
    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("")
    }
    // instrumentCode relies on a JAR removed in IntelliJ 2024.1+; disable it.
    // (Only affects Java null-check bytecode injection — not needed for Kotlin.)
    instrumentCode {
        enabled = false
    }
}
