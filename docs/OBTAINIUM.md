# Obtainium updates

ProSpaceHub uses ordinary tagged GitHub releases with an APK asset. Obtainium supports this source directly; an in-app network updater is unnecessary.

Source: [vertig0o0/DaylightHubActual](https://github.com/vertig0o0/DaylightHubActual).

[Add ProSpaceHub to Obtainium](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/vertig0o0/DaylightHubActual)

Alternatively, open Obtainium → Add app and paste the repository URL. Keep prereleases disabled for stable updates. Select the ProSpaceHub APK asset when prompted. Android may ask permission for Obtainium to install apps; this permission belongs to Obtainium, and ProSpaceHub remains offline.

## Release contract

- Keep application ID `dev.daylight.hub` and the existing signing identity.
- Increase Android versionCode for every published APK; do not reuse version codes for different public builds.
- Use a stable version tag, a clearly named universal APK containing ARM64 and x86_64, a SHA-256 checksum file, and concise release notes.
- Publish the APK as a release asset, rather than only a workflow artifact. Stable updates must be a published stable release, not a draft/prerelease.
- Test an in-place update from the previous public APK with populated data. Never require uninstalling to upgrade.
- Keep the signing private key out of Git history and public release assets. Verify the certificate against the previous release before publication.

Existing v2.0.1 installations are signed with SHA-256 certificate fingerprint:

`d8696c1e43623861dc463091665b75c4a7ad757ee78e2586fa0e6d2d2fc13a30`

v3.0.0 uses versionCode5 and a single production APK named `ProSpaceHub-v3.0.0.apk`, accompanied by `SHA256SUMS.txt`. Its package/signature/alignment and in-place data checks are recorded in [VALIDATION.md](../VALIDATION.md). The release build rejects a different signing certificate, so a fresh developer checkout cannot silently produce an incompatible public update.

The public-source discovery and installation audit is performed after the asset is published; its outcome is recorded in the validation document. Updates never require ProSpaceHub itself to access the network.

References: [Obtainium's source documentation](https://wiki.obtainium.imranr.dev/sources/) and [deep-link documentation](https://wiki.obtainium.imranr.dev/deep_links/).
