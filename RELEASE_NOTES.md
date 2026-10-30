# ProSpaceHub 1.1.0 — Fully offline upgrade

Native on-device AI, rich linked notes, a calendar with local reminders, and expanded appearance controls.

## What's included

- Import a GGUF from device storage and run it with the pinned llama.cpp CPU runtime in a separate private process. Stream replies, stop generation, switch models, and save per-model generation parameters.
- The installed app has no Internet permission, endpoint setting, localhost server, or model downloader. Notes and calendar remain available without a loaded model.
- AI actions use whitelisted tools. Every proposed creation or change requires explicit review; approvals are single-use and reject stale item revisions.
- A dedicated rich writing page with autosave, notebooks, tags, search, formatting, note links, and backlinks.
- Month, week, day, and agenda views; recurring events and occurrence exceptions; notification or ringing reminders with explicitly chosen 30-minute lead increments, snooze, and dismissal.
- Light, Dark, and System themes, custom palettes and backgrounds, text scaling, editor fonts, spacing, and navigation customization.
- A non-destructive Room v1-to-v2 migration retaining existing notes and dated tasks. Existing accent/compact preferences are retained.

## Download and setup

Download **ProSpaceHub-1.1.0-debug.apk** below. Android 8.0+ on ARM64 is supported; x86_64 is included for emulators. No model weights are bundled.

Import a supported text/chat GGUF in **Settings → Local models → Import GGUF**, then load it and open Assistant. See [README.md](https://github.com/vertig0o0/DaylightHubActual/blob/v1.1.0/README.md) for build and model-import instructions.

**This is a debug-signed preview build.** Updating an existing installation while preserving its private data requires the same signing key. If Android reports a signature mismatch, rebuild using the original key; uninstalling removes the installed app's private data.

## Validation

- Build completed successfully; 13 unit tests and 10 emulator instrumented tests passed.
- Real local GGUF generation, cancellation, context-limit rejection, and recovery after terminating the inference process were tested in airplane mode.
- Migration, approval safeguards, rich documents, dark-mode UI, and basic local alarm delivery/snoozing passed their automated checks.
- Lint: 0 errors, 14 warnings. APK ZIP alignment and ARM64 ELF alignment were checked for 16 KB compatibility.
- Physical 8 GB ARM64 performance, larger models, Android 8/tablet execution, and device/OEM alarm lifecycle behavior still need hardware acceptance checks. See [VALIDATION.md](https://github.com/vertig0o0/DaylightHubActual/blob/v1.1.0/VALIDATION.md).

SHA-256 (APK):

```text
C4C307D864AA88AD346FC49021A0D0265B1C2555CD433703DB4CA8B232E8CBB8
```
