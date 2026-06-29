# Sound Plugin

An Android Studio / IntelliJ IDEA plugin that plays a short audio tone when a project build finishes.

## Features

- **Success**: low → high two-note ascending chime (659 Hz → 880 Hz)
- **Failure**: high → low two-note descending tone (440 Hz → 294 Hz)
- Tones have a fade-in/out envelope to avoid audio clicks
- No WAV files needed — tones are generated via `javax.sound.sampled`

## Settings

Open **Settings → Tools → Build Sound** to configure:

- Enable/disable toggle
- Separate success / failure toggles
- Volume slider (0–100)
- Tone duration slider (80–400 ms)
- "Test success sound" and "Test failure sound" buttons

## Project Structure

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Gradle build targeting IntelliJ 2024.1 / JVM 17 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 8.6 |
| `src/main/resources/META-INF/plugin.xml` | Declares plugin ID, listener registration, settings extension |
| `BuildSoundSettings.kt` | Persistent app-level state (saved to `build-sound.xml`) |
| `SoundPlayer.kt` | Generates tones via `javax.sound.sampled` |
| `BuildSoundListener.kt` | Implements `ProjectTaskListener.finished()` — fires on every Gradle/Make build |
| `BuildSoundConfigurable.kt` | Settings UI under **Settings → Tools → Build Sound** |

## Build & Install

### Build the plugin zip

```bash
cd build-sound-plugin
./gradlew buildPlugin
```

Output: `build/distributions/build-sound-plugin-1.0.0.zip`

### Install in Android Studio

1. Open **Settings → Plugins**
2. Click the **⚙** gear icon → **Install Plugin from Disk…**
3. Select the zip file above

### Run in a sandboxed IDE (for development/testing)

```bash
./gradlew runIde
```

## Targeting Android Studio vs IntelliJ

In `build.gradle.kts`, the `type` setting controls which IDE is used for sandboxing:

```kotlin
intellij {
    version.set("2024.1")
    type.set("IC")   // "IC" = IntelliJ Community, "AI" = Android Studio
}
```

Switch to `"AI"` and set `version` to the matching Android Studio build number to sandbox directly inside Android Studio.
