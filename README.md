# ProSpaceHub / Daylight Hub for Android

A fully offline Android workspace with projects, tasks and calendar, rich linked notes, focus timers, a private reading library, and optional local GGUF assistance. **v3.0.0, version code 5** introduces Today / Planner / Notes / Reading / Assistant in an adaptive Material 3 interface. Application ID remains **dev.daylight.hub**, and existing installations retain their signing identity and private data.

Stable APKs are distributed through [GitHub releases](https://github.com/vertig0o0/DaylightHubActual/releases). [Obtainium setup](docs/OBTAINIUM.md) explains automatic update checks. ProSpaceHub itself has no network permission.

## Build on Windows

Prerequisites: Git, PowerShell 7, Android Studio with its bundled JBR (JDK 17+), Android SDK platform 37 and build tools. The project uses AGP 9.5.0, Gradle **9.8.2**, Kotlin/Compose compiler 2.4.21, NDK r30 **30.0.16248370**, and CMake **3.31.6**.

From the repository root:

~~~powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
pwsh -File tools/prepare-native.ps1
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
# For the production variant, using the original installation's local signing key:
.\gradlew.bat :app:assembleRelease
~~~

The preparation script downloads checksum-pinned build tools and the exact llama.cpp revision into ignored local folders, and writes ignored local.properties. Build-time downloads happen on the development computer. The installed app has **no network permission** and downloads nothing.

APKs: app/build/outputs/apk/debug/app-debug.apk and app/build/outputs/apk/release/app-release.apk. ARM64 phones and x86_64 emulators are included; Android 8.0/API 26 remains the minimum. Native libraries use 16 KB ELF alignment. Runtime testing on a 16 KB device is documented separately from alignment checks.

The published v3 APK uses the non-debuggable release variant and preserves the certificate used by the earlier public debug builds. The original private key is local and is never committed. Gradle's release signing configuration references that existing local key and rejects a different certificate. A fresh checkout can build the debug variant for development; its automatically generated key cannot sign public updates. If Android reports a signature mismatch, rebuild with the original key; uninstalling removes private notes, plans, books, and models.

Reader JavaScript and notices are committed under app/src/main/assets/reader. To change them, install Node and run `pwsh -File tools/prepare-reader.ps1`; the script uses the pinned pnpm lockfile. Original MIT Foliate sources remain in reader/vendor/foliate, with reviewed Android compatibility patches in reader/build.mjs. No Node runtime is installed on the phone.

llama.cpp is pinned to **889f2ec96deacbc2d8a9a67dbe181eaa43a2fb3b** and CMake verifies the checkout revision. Server, tool, example, subprocess, and HTTPS components are disabled. CPU inference runs in a non-exported :inference service process through JNI; no localhost server or external runtime app is used.

The AGP 9.5 BidiSpoofing lint visitor was observed looping in Kotlin PSI comment traversal. That one detector is disabled; the preBuild dependency verifySourceText rejects bidirectional control characters directly in the source. Other lint checks remain enabled.

## Import and run a model

1. Obtain a supported decoder **text instruct/chat GGUF** on your device through your own file transfer.
2. Open **Settings → Local models → Import GGUF** and choose a local file.
3. Wait for copying and header validation. The app keeps its own private copy and displays GGUF metadata.
4. Open **Parameters** to tune the model; then choose **Load**.
5. Open **Assistant**. Replies stream locally; **Stop** cancels generation. Notes and calendar remain available without loading any model.

Loading shows its current stage: native runtime startup, GGUF metadata, mapping weights, then context/CPU preparation. The percentage applies to weight mapping; context preparation uses an indeterminate indicator. Elapsed time and **Cancel loading** are shown in Settings. A cancelled or unresponsive load can restart only the inference process. A three-minute loading timeout reports the last stage and allows another attempt.

If loading still fails on your phone, report the last stage or error shown. The original loading race is fixed by copying Android IPC messages before dispatching work; late replies are matched to their request IDs. Native loading uses memory-mapped weights and disables optional weight repacking to reduce startup work and memory overhead.

The focused skills are designed for approximately **230M–360M text instruct/chat models**, with a native 512-input-token limit and short bounded outputs. New profiles default to two CPU threads; existing profile parameters are retained. Model architecture, RAM requirements, speed, and suggestion quality vary. Base models without a supported embedded chat template, encoder models, and unsupported GGUF architectures are rejected with an error. Physical ARM64 performance remains unverified; emulator results are in [VALIDATION.md](VALIDATION.md).

Profiles save temperature, top-p, top-k, min-p, repetition penalty, seed, reply length, context size, CPU threads, and system prompt. Sampling changes apply to the next reply. Reload after changing context or threads. Inference stops when the app goes into the background; scheduled reminders do not depend on inference.

**Allow app tools** enables local note/calendar search, opening items, and proposed writes. Plain chat mode streams without tools. Imported content is reference data, not authorization. Every AI creation, edit, deletion, completion change, occurrence change, or settings write is presented for review. Approvals are single-use and tied to the exact item revision. Cancelling or restarting cannot apply an outstanding proposal.

No model weights are bundled.

## Projects, ready tasks, and focus

Planner contains Ready, Tasks, Projects, and the existing calendar. A task, note, or conversation has one optional project; unassigned items stay in Inbox. Projects can be pinned, archived, edited, or removed while retaining their content. Templates create independent notes/tasks, remap internal links and prerequisites, preserve labels and relative calendar deadlines, and expose missing external prerequisites as blocker notes. Template snapshots are bounded to keep them usable offline.

Tasks support subtasks, priority, optional deadlines and estimates, a label, manual blocker notes, and multiple prerequisites. Transactional graph checks prevent self-links and cycles, including concurrent edits. Reopening or removing a prerequisite blocks dependent work again. Clearing a manual blocker does not bypass prerequisites. Ready ordering supports priority, deadline, duration, and manual order, with 5/15/30/60/custom-minute filters and a separate view for tasks without estimates.

Focus sessions attach to a task or project, with pause/resume, early end, breaks, paged history, and totals. Timers use monotonic time, survive ordinary process restarts, and pause at their last checkpoint after reboot. Continue-here notes can link to a task, project, or note and appear on Today. A local model is never needed for timers or calculations.

## Optional contextual suggestions

Task details can request up to three editable starting steps and save them explicitly as subtasks. Notes offer title suggestions, one label from a selected set of up to eight, and selected-text clearer wording, punctuation, or shortening. Continue-here text can be shortened to one sentence. Generation is stateless, cancellable, and receives only the selected short source and optional brief project description.

Suggestions show their source before Apply. Allowed-label membership, output limits, placeholders, and malformed responses are checked in app code. Applying requires the captured source/revision to remain current; repeated approval cannot apply twice. Selected-text changes participate in editor undo, and title changes offer Undo. Small models may make unhelpful suggestions or decline a label; review and manual editing remain essential. Chat history is saved per conversation, displayed in bounded windows, and only the last four model-context messages are retained in a request.

## Reading

Reading has a paged library, reader, and journey view with statuses, time history, activity heatmap, finished-book timeline, and optional goals. Import through the file picker or Open with. The app keeps a private SHA-256-identified copy, so moving or deleting the original does not remove the book or its position.

DRM-free EPUB, PDF, recognized MOBI/AZW/AZW3, FB2/FB2 ZIP, TXT, HTML/XHTML, RTF, CBZ, and supported CBR archives have offline reading paths. The [format matrix](docs/READING.md) distinguishes direct rendering, conversion, and explicit limitations, including unsupported KFX/DRM/DjVu and legacy containers. PDFs/comics use actual pages; reflowable books use stable locators across font changes. Reading never uses the LLM or sends book text to it.

## Notes

- Dedicated writing page, searchable menu, notebook names, comma-separated tags, and title/recent sorting.
- Bold, italic, underline, strike, headings, numbered/bullet lists, colors, highlights, and undo/redo.
- Versioned rich document JSON plus a plain-text search/AI projection. Existing plain text stays literal and is converted when edited.
- Autosave with Saved/Saving/error status. Editor state survives rotation and navigation.
- Use the link button or type [[ to find a note. Internal links use stable IDs; backlinks are derived from saved links. Renaming does not break links. Deleted targets show an unavailable message.
- Tablet layout keeps the note menu beside the editor.
- External URLs can be stored as text/links but are not opened by the app.

## Planner and alarms

Month, week, day timelines, and agenda views support timed/all-day events, overlapping blocks, local time zones, and daily/weekly/monthly repeats. Monthly dates absent from a month are skipped. Edit/delete one occurrence or the whole series. Legacy tasks retain dates and completion state.

Every new event and AI event review asks for the reminder lead time: Off, At start, 30, 60, 90 minutes, and further multiples of 30. Choose notification or ringing alarm. All-day reminders use 09:00 in the event time zone as their reference.

Enable Android notification permission and **Alarms & reminders** special access in Settings. Exact alarms are scheduled through AlarmManager, rescheduled after reboot/time changes/updates/permission restoration, and reconciled after edits and deletions. A permission problem is displayed instead of claiming reminders are active.

Ringing alarms use local alarm audio. Concurrent alarms are grouped without losing titles. Dismiss stops the group; Snooze moves the group 30 minutes forward. Snoozes retain event revisions and survive scheduling rebuilds. A ten-minute timeout silences audio and posts missed-alarm notices.

Android cannot deliver alarms while a device is powered off or an app is force-stopped. Reopening rebuilds future schedules. Sound/DND and notification channels remain under the device owner's Android settings.

## Appearance

Light, Dark, and System themes; separate custom light/dark accents, surfaces, and backgrounds; text scaling; compact spacing; editor fonts; solid/gradient/local-image backgrounds; navigation visibility and ordering; and reset controls. Settings is always reachable. Imported backgrounds are resized and copied into private storage.

## Validation

See [VALIDATION.md](VALIDATION.md) for the v3 host/device results, release checks, measured emulator diagnostics, and explicitly unverified physical-device behavior. The user accepted emulator-only completion for this release.

~~~powershell
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
# With an emulator or physical device connected:
.\gradlew.bat :app:connectedDebugAndroidTest
~~~

Tests cover single-use/rejected/stale approvals, validated parameters and GGUF headers, recurrence/DST/month boundaries, migration from a populated v1 database, backlinks, no network permission, rich-document round trips, and saving/reopening a note through Compose UI.

NativeInferenceTest is an optional real-model integration test: it expects a supported small chat GGUF at the app-private path files/models/smoke.gguf. Supply this using adb run-as in a debuggable test installation. It exercises native loading, streamed text, cancellation, constrained JSON, and unloading. It skips when the file is absent; it never downloads a model.

NativeLoadingTest additionally expects SmolLM2-360M-Instruct Q4_K_M at files/models/smoke360.gguf and LiquidAI LFM2.5-230M Q4_K_M at files/models/lfm230.gguf. It checks actual load progress, text generation, cancelling/retrying, and recovery after invalid files. InferenceMessageTest checks that queued requests survive Android recycling the original Message.

Inspect app/build/reports and app/build/test-results for check results. Hardware performance, battery behavior, OEM alarm restrictions, and large-model memory limits require testing on the intended phone.

## Data and source layout

Room v5 uses additive, explicit v1→2→3→4→5 migrations, preserving IDs, timestamps, note text, dated tasks/completion, models and saved preferences. New tables store projects, prerequisites, templates, conversations, focus/recovery data, and reading. No destructive migration fallback is enabled. Automatic cloud backup and device transfer remain excluded.

- data/: Room schema, migration, shared repositories.
- ai/: GGUF import, profile validation, JNI service/client, tool routing, approvals.
- calendar/: RFC 5545 recurrence, scheduling, receivers, snoozes, ringing audio.
- focus/: monotonic timers, notifications, handoffs and recovery.
- reading/: private imports, native/WebView readers, paging and active-time tracking.
- navigation/: typed versioned saved navigation state.
- ui/: notes, calendar, assistant, settings, theme, adaptive app shell.
- src/main/cpp/: native build and JNI runtime.
- tools/: reproducible native setup.
