# ProSpaceHub v2.0.1 — GGUF model loading fix

Fixes a race that could leave **Load** waiting indefinitely: Android could recycle an IPC Message before the background worker read its command. Requests are now copied before queueing, and replies are matched to their request IDs.

## Changes

- Native-library initialization runs on the inference worker, keeping the service responsive during startup.
- Loading shows its stage, weight-mapping progress, and elapsed time.
- **Cancel loading** can recover by restarting the separate inference process.
- A three-minute timeout reports the last stage and allows another load attempt.
- Memory-mapped weights avoid optional CPU weight repacking during startup.
- Completion clears the service's busy state before replying, preventing a subsequent operation from racing the previous one.
- Rotation preserves a model load already in progress.

## Download and install

Download **ProSpaceHub-v2.0.1-debug.apk** and **SHA256.txt** below. The app displays **v2.0.1**, version code 4. ARM64 and x86_64 are included; Android 8.0+ remains supported. No model weights are bundled.

Install as an update to the previous APK. The signing key and application ID are unchanged; existing notes, plans, settings, and imported model files are retained. This remains a debug-signed build.

## Validation

- Build and lint passed: 0 lint errors, 14 warnings.
- 18 unit tests and 15 Android instrumented tests passed, with no failures or skips.
- Regression tests cover Android message recycling, stalled-load timeout/recovery, cancellation/retry, stale replies, invalid files, and inference-process failure.
- SmolLM2-360M-Instruct Q4_K_M and LiquidAI LFM2.5-230M Q4_K_M both loaded and generated replies with a 2,048-token context and four CPU threads. The existing SmolLM2-135M test also passed.
- Device tests ran in airplane mode on an API 37 x86_64 emulator. Physical ARM64 phone verification remains outstanding.
- 16 KB APK ZIP alignment and ARM64 native ELF load alignment were checked.

See [README.md](https://github.com/vertig0o0/DaylightHubActual/blob/v2.0.1/README.md) and [VALIDATION.md](https://github.com/vertig0o0/DaylightHubActual/blob/v2.0.1/VALIDATION.md) for instructions and test scope.

SHA-256 (APK):

```text
723E8F863D6084412F929367410E013010EA1BD79C500F1CE6990E4F37414B32
```
