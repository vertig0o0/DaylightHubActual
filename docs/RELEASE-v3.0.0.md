# ProSpaceHub v3

v3 brings projects, focused work and offline reading into one adaptive Material 3 app.

- **Today / Planner / Notes / Reading / Assistant**, with tablet layouts, keyboard-aware editors, accessible themes, reduced motion and deletion Undo.
- **Projects and independent templates**, subtasks, multiple prerequisites with cycle prevention, manual blockers, ready queues, manual ordering and available-time filters.
- **Focus timers**, pause/resume, breaks, history/totals and Continue-here cards for tasks, projects and notes.
- **Optional small-model suggestions** for starting steps, titles, allowed labels, selected-text cleanup and shorter handoffs. Suggestions are cancellable and editable, with explicit Apply and stale-source protection.
- **Persistent project conversations**, bounded recent model context and the existing single-use approval flow for app tools.
- **Private Reading library and journey** with search, bookmarks, stable positions, themes, reading time, goals and history. Offline paths cover EPUB, PDF, recognized MOBI/AZW/AZW3, FB2/FB2 ZIP, TXT, HTML, RTF, CBZ and supported CBR archives. DRM/KFX, DjVu and other limitations are documented in the [format matrix](https://github.com/vertig0o0/DaylightHubActual/blob/master/docs/READING.md).

The APK has **no network permission** and contains no model weights. Small-model suggestion quality varies; review remains essential.

## Install and update

Download **ProSpaceHub-v3.0.0.apk** and its **SHA256SUMS.txt** checksum. This single APK supports ARM64 and x86_64 on Android 8.0/API 26 or newer. It is the non-debuggable production variant, signed with the original update identity, versionCode **5**. Install over the existing app to retain its data.

For future updates, add [this repository](https://github.com/vertig0o0/DaylightHubActual) to Obtainium, or use **Settings → About → Add to Obtainium**. No in-app network updater is needed.

Migration, native inference, reader corpus, lifecycle, UI and offline checks are recorded in [VALIDATION.md](https://github.com/vertig0o0/DaylightHubActual/blob/master/VALIDATION.md). **Physical 8 GB ARM64 performance and actual 16 KB page runtime remain unverified**; native ELF/ZIP alignment is verified. The user accepted emulator-only completion for this release.
