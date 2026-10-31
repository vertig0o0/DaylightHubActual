# ProSpaceHub v3 validation

Verified 31 October 2026. Package `dev.daylight.hub`, version `v3.0.0`, versionCode **5**, Room schema **5**. The user accepted emulator-only completion; target-phone ARM64 performance remains unverified.

## Release artifact

- `ProSpaceHub-v3.0.0.apk`: one non-debuggable release APK for ARM64 and x86_64, Android 8.0/API 26 minimum, target SDK 37.
- SHA-256: `58ed33f1c97e3117083a1b6e6f78d64237a306453ed270874f7c3dd5ab74da1b`.
- Original signing certificate retained: `d8696c1e43623861dc463091665b75c4a7ad757ee78e2586fa0e6d2d2fc13a30`.
- The certificate has the historical Android Debug subject used by v2; the v3 application itself is **not debuggable**. Release signing now rejects a different certificate. No private key is committed or distributed.
- No INTERNET permission, cloud backup, account, analytics or bundled model weights. All reader runtime assets and notices are local.

## Automated results

| Check | Result |
| --- | --- |
| Production APK / matching instrumented-test APK | Built successfully |
| JVM tests | 45 passed, 0 failed |
| Reader Node checks | 6 passed, 0 failed |
| API 26 production-APK suite | 56 passed, 0 failed; one v2-only seeding assumption skipped (runner reports 57 tests) |
| Android release lint | 0 errors, 26 warnings |
| APK 16 KB ZIP alignment |`zipalign -c -P 16 4` passed |
| Native ELF alignment |All LOAD segments of all 12 packaged ARM64/x86_64 libraries use 0x4000 alignment |
| Source whitespace |Passed; original vendor/license whitespace retained with narrow attributes |

The production suite includes native streaming, cancellation, constrained JSON, input overflow, inference-worker death/reload,230M/360M loading and skills, migration/approval protection, notes/calendar/conversation UI, workspace DAG/concurrency/templates/manual ordering, focus and reading lifecycle, import recovery, and reader corpora. The old seeding test deliberately runs only against v2, and passed separately before the genuine in-place upgrade.

The native tests use available CPU cores and kill only the target UID's separate inference worker, so they also run against the non-debuggable APK. Test harness assumptions about four cores and `run-as` were corrected before the final pass.

One preexisting AGP 9.5 BidiSpoofing detector is disabled because its Kotlin PSI visitor looped; `verifySourceText` instead rejects actual bidirectional control characters in source. Other lint checks remain enabled. Remaining warnings concern library-version suggestions, Kotlin/Compose style, programmatic View construction, storage preflight suggestions and existing empty-super calls; there are no lint errors.

## Data and update preservation

A fresh API 26 emulator first installed the unchanged public v2.0.1 APK (code 4/schema 2). A pure-Java test seeded stable note/task IDs, Unicode/literal markup, task date/completion, notebook/tags/revisions, a model profile with its exact parameters, private model sentinel bytes and custom appearance/navigation. Installation used Android's in-place update, without uninstalling. The migration and subsequent production updates retained those values. A separate populated v1→5 migration verifies all schemas and sqlite_sequence high-water preservation after deletion.

The final APK has the same package/certificate and a higher versionCode than the public v2 release. [Obtainium setup](docs/OBTAINIUM.md) describes the GitHub source. End-to-end discovery/installation of the published asset is recorded after publication, using the preserved API 37 installation.

## Reader coverage

API 26 (WebView/Chrome 69) and earlier API 37 checks cover genuine unchanged Gutenberg Alice EPUB3, MOBI and AZW3 files. Tests exercise metadata, contents, search, navigation, stable location across font changes/reopen, fixed-layout EPUB zoom/page restoration, publication scripts blocked, UTF-8 and declared Windows-1251 FB2 text, private-copy imports, duplicates, bookmarks, source removal/repair, RTF conversion,19 MB streaming FB2 normalization, PDF render/search/text and comic numeric ordering/RAR conversion/traversal rejection. Merged intent resolution includes the AZW3 MIME type and excludes DjVu.

See [READING.md](docs/READING.md) for direct/conversion/unsupported paths, resource limits and fidelity limits. TXT supports UTF-8/UTF-16 BOMs; other legacy TXT encodings require external conversion. HTML retains basic structure and declared charset handling. Large FB2 and RTF normalization preserves text. Password-protected PDFs need an unlocked copy. No DRM removal or universal-format claim is made.

## UI and performance observations

Phone and tablet visual checks cover navigation, a two-pane note layout, readable dark/large text, wrapping primary actions, editor keyboard behavior and recovery. During display-density switching, the old emulator Gboard crashed once while reinflating its own layout and recovered on restart; the app remained intact. Android 8's activity window is explicitly resized for the keyboard. The keyboard regression requires Save to stay visible and at least 96 dp of writing area with the actual keyboard open. Note details and chat tools use contextual sheets; navigation/secondary controls collapse while typing. Automated note editing/reopening and large-text calendar tests pass.

A bounded paging stress check populated 10,001 books, 1,000 notes (~7 MB body text) and 1,000 sessions. Book/note pages stayed at 32 records and history at 40; consecutive book pages did not overlap; literal Unicode search worked. On the API 26 x86_64 emulator, seeding took 10.5 s and the page/search checks 259 ms.

A separate debug-APK diagnostic kept the UI interactive through six swipes while a 360M model generated throughout (1,024-token context, two CPU threads). It recorded 18 software-rendered frames: median 40.25 ms, 95th percentile 111.11 ms, maximum 112.93 ms; main-process PSS 149,440 KB and inference-process PSS 341,326 KB. These numbers are emulator diagnostics, **not a 60 fps claim or a prediction for the phone**. The production suite repeats the functional overlap check.

Small-model outputs remain variable. The 230M fixture produced a useful travel title but left punctuation unchanged and chose an uncertain label; 360M classified the Waiting label and fixed punctuation but sometimes returned a truncated title. Both may repeat a short task as one starting step. Suggestions remain editable, cancellable and explicitly applied; semantic accuracy is not guaranteed. Stale sources, malformed output, placeholders and instruction echoes are rejected without automatic writes.

## Explicitly unverified

- Physical 8 GB ARM64 timing, frame rate, peak memory, thermals and battery use.
- Actual runtime on a 16 KB page device; ELF and ZIP alignment are verified.
- OEM-specific background/alarm restrictions and a complete manual TalkBack audit. Emulator tests cover labels, review/cancellation behavior, larger text and contrast checks.

Historical release results remain in [v2.0.1 validation](docs/VALIDATION-v2.0.1.md). Local detailed logs are under ignored artifacts/v3; no user-private databases, models or keys are release assets.