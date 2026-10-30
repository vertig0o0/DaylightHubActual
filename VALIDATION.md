# ProSpaceHub 1.1.0 validation

Verified on 30 October 2026. The application ID remains `dev.daylight.hub` and the database migrates from Room version 1 to 2.

## Delivered build

- APK: `artifacts/ProSpaceHub-1.1.0-debug.apk` (58,133,074 bytes).
- SHA-256: `C4C307D864AA88AD346FC49021A0D0265B1C2555CD433703DB4CA8B232E8CBB8`.
- Debug signed; ARM64 and x86_64; minimum Android 8.0/API 26.
- AGP 9.5.0, Gradle 9.8.2, NDK r30, CMake 3.31.6.
- llama.cpp revision `889f2ec96deacbc2d8a9a67dbe181eaa43a2fb3b`.

To upgrade an existing installation while retaining private data, sign with the same key used by that installation. A signature mismatch requires rebuilding with that key. Uninstalling removes private data.

## Results

| Check | Result |
| --- | --- |
| Debug APK and instrumented-test APK builds | Passed |
| JVM unit tests | 13 passed, 0 failed, 0 skipped |
| Android instrumented tests | 10 passed, 0 failed, 0 skipped |
| Android lint | 0 errors, 14 warnings |
| Merged application manifest | No INTERNET permission; cleartext traffic and backup disabled |
| 16 KB APK ZIP alignment | Passed `zipalign -c -P 16 4` |
| ARM64 native ELF load alignment | 0x4000 (16 KB) |
| Source whitespace check | Passed `git diff --check` |

One AGP 9.5 lint detector, BidiSpoofing, is disabled because its Kotlin PSI visitor looped during analysis. The build runs `verifySourceText` to reject bidirectional control characters directly. Other lint checks remain enabled.

## Automated coverage

Unit tests cover single-use approvals, rejection and invalidation, concurrent approval attempts, stale revisions, explicit reminder values, parameter validation/serialization, invalid GGUF headers, safe partial reply display, monthly missing dates, daylight-saving transitions, recurrence exceptions, repeat ending dates, and all-day reminder reference times.

Instrumented tests cover:

- Populated version-1 database migration, retained note IDs/text/timestamps and task dates/completion flags, and Room schema validation.
- Search-index updates and stable backlinks through note renaming and target deletion.
- No network permission in the installed application.
- Rich JSON round trips for formatting, colors, highlights, internal links, and emoji.
- Saving and reopening a formatted note through the actual Compose editor.
- Dark calendar with 130% text size and switching to the day view; its screenshot was visually inspected for readable text.
- Real local GGUF loading, streamed replies, cancellation, constrained JSON output, context overflow rejection, inference-process termination/reconnection, reloading, and unloading.
- AlarmManager notification delivery, two overlapping ringing alarms, grouped 30-minute snoozes, and cancellation after event deletion.
- Task completion and deletion approvals; rejection, replay, and stale-revision failures; note content containing instructions cannot grant write permission.
- Event creation with explicitly reviewed reminder values, occurrence rescheduling/deletion, stale series revisions, and entire-series deletion.
- Reading full planner details and opening a specific task or occurrence without mutating it.
- Stale model-parameter and appearance approvals, parameter persistence, and retention of legacy accent/compact preferences.

The device suite ran in airplane mode on an API 37 x86_64 emulator with 4 GB RAM and four virtual CPUs. The native test used a locally supplied SmolLM2-135M-Instruct Q4_K_M GGUF. That test model is not included in the APK. Process failure was tested by killing only the separate inference process; an actual out-of-memory condition was not induced.

## Reproduction

Build and unit/lint checks:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest :app:lintDebug --offline --no-daemon
```

After installing both APKs on the emulator and providing the optional local smoke model:

```powershell
adb shell cmd connectivity airplane-mode enable
adb shell am instrument -w -r -e disableAnalytics true dev.daylight.hub.test/androidx.test.runner.AndroidJUnitRunner
```

The final build and device logs, lint report, unit-test XML reports, and checksum are copied to `artifacts/`. See README.md for toolchain preparation and model import instructions.

## Physical-device acceptance still needed

No physical ARM64 phone was connected. The following remain device checks rather than claimed test results:

- Real 8 GB phone performance, temperature/battery usage, larger GGUF imports and model switching, and actual memory exhaustion.
- Android 8.0 execution, tablet layout, and ARM64 execution with 16 KB system pages (packaging and ELF alignment were checked).
- Reboot, clock/time-zone changes, app updates, Doze, permission denial/revocation/restoration, manufacturer background restrictions, and ten-minute alarm timeout with real audio/DND behavior.
- Storage exhaustion, interrupted file-provider transfers, and imported image backgrounds across device providers.

Calendar recurrence/DST behavior and permission-aware scheduling are implemented; the deterministic recurrence tests and basic live alarm tests above do not replace those lifecycle and hardware checks.
