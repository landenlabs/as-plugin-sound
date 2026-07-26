<table border="0">
  <tr>
    <td>
      <!-- VERSION -->v6.07.21<br>
      <!-- DATE -->26-Jul-2026<br>
      IntelliJ Platform Plugin (Kotlin)<br>
      <a href="https://github.com/landenlabs/as-plugin-sound">Repo</a>
    </td>
    <td>
      <a href="https://landenlabs.com">
        <img src="screens/landenlabs_400.webp" width="300" alt="LanDen Labs">
      </a>
    </td>
  </tr>
</table>

# Sound Plugin

An Android Studio / IntelliJ IDEA plugin that plays a short audio tone when a
project build finishes.

**By [LanDen Labs](https://github.com/landenlabs) (2026)**

---

## Features

- **Success**: low → high two-note ascending chime (659 Hz → 880 Hz)
- **Failure**: high → low two-note descending tone (440 Hz → 294 Hz)
- Tones have a fade-in/out envelope to avoid audio clicks
- No WAV files needed — tones are generated via `javax.sound.sampled`

---

## Android Studio Compatibility

Verified working on **Android Studio Quail 2 | 2026.1.2 Patch 1** (`AI-261.25134.95.2612.15914620`, built July 22, 2026).

The plugin declares `sinceBuild = "241"` with no `untilBuild` cap (see [`build.gradle.kts`](build.gradle.kts)), so it targets platform build **241 and newer** with no upper limit. Each Android Studio release is built on a specific IntelliJ Platform branch (the number before the first dot in its build number, e.g. `261.xxxxx...` → branch `261`) — as long as that branch is ≥ 241, the plugin will load.

| Android Studio release | Version | Platform branch | Runs this plugin? |
|---|---|---|---|
| Quail 2 | 2026.1.2 | 261 | ✅ Yes (verified) |
| Quail 1 | 2026.1.1 | 261 | ✅ Yes |
| Panda (latest is Panda 4) | 2025.3.x | 253 | ✅ Yes |
| Otter (incl. Feature Drops) | 2025.2.x | 252 | ✅ Yes |
| Narwhal (incl. Feature Drops) | 2025.1.x | 251 | ✅ Yes |
| Meerkat | 2024.3.1 | 243 | ✅ Yes |
| Ladybug | 2024.2.1 | 242 | ✅ Yes |

Official release notes: [developer.android.com/studio/releases](https://developer.android.com/studio/releases) (older versions: [past releases archive](https://developer.android.com/studio/releases/past-releases)).

---

## Settings

Open **Settings → Tools → Build Sound** to configure:

- Enable/disable toggle
- Separate success / failure toggles
- Volume slider (0–100)
- Tone duration slider (80–400 ms)
- "Test success sound" and "Test failure sound" buttons

---

## Project structure

```
as-plugin-sound/
├── build.gradle.kts                          # Gradle build targeting IntelliJ 2024.1 / JVM 17
├── gradle/wrapper/gradle-wrapper.properties   # Gradle 8.6
├── src/main/resources/META-INF/plugin.xml     # Declares plugin ID, listener registration, settings extension
└── src/main/kotlin/com/dennislang/buildsound/
    ├── BuildSoundSettings.kt      # Persistent app-level state (saved to build-sound.xml)
    ├── SoundPlayer.kt             # Generates tones via javax.sound.sampled
    ├── BuildSoundListener.kt      # Implements ProjectTaskListener.finished() — fires on every Gradle/Make build
    └── BuildSoundConfigurable.kt  # Settings UI under Settings → Tools → Build Sound
```

---

## Build & Install

### Build the plugin zip

```bash
cd as-plugin-sound
./gradlew buildPlugin
```

Output: `build/distributions/build-sound-plugin-1.0.0.zip` (name comes from `rootProject.name` in `settings.gradle.kts`)

### Install in Android Studio

1. Open **Settings → Plugins**
2. Click the **⚙** gear icon → **Install Plugin from Disk…**
3. Select the zip file above

### Run in a sandboxed IDE (for development/testing)

```bash
./gradlew runIde
```

---

## Targeting Android Studio vs IntelliJ

In `build.gradle.kts`, the `type` setting controls which IDE is used for sandboxing:

```kotlin
intellij {
    version.set("2024.1")
    type.set("IC")   // "IC" = IntelliJ Community, "AI" = Android Studio
}
```

Switch to `"AI"` and set `version` to the matching Android Studio build number to sandbox directly inside Android Studio.

---

## Releasing

Versions are bumped with `set-version.bash` (or `set-version.ps1` on
Windows), run from the repo root:

```bash
./set-version.bash -version 1.0.1 -message "Add volume slider"
```

This updates `VERSION`, `src/main/resources/META-INF/plugin.xml`'s
`<version>`, and the `<!-- VERSION -->v6.07.21<!-- DATE -->26-Jul-2026
markers above, then commits, tags, and pushes.
`build.gradle.kts`'s Gradle project `version` always reads `VERSION`
directly, so it never needs a separate bump. Pushing the resulting `vX.Y.Z`
tag triggers `.github/workflows/release.yml`, which builds the plugin zip and
publishes it to a GitHub Release — no signing secrets required since the zip
isn't published to the JetBrains Marketplace by this workflow.

---

## License

Apache 2.0 © [LanDen Labs](https://github.com/landenlabs) 2026
