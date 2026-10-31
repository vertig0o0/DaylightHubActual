# Offline reader runtime

The Android host supplies bounded ZIP entries or file ranges from its private book copies. The browser bundle never fetches a complete book or uses a network server. `src/reader.html` is served at the reserved appassets origin; a main-frame WebMessagePort connects the trusted wrapper to the Android host. Publication frames do not receive script privileges.

Upstream Foliate source is vendored at revision `78914aef4466eb960965702401634c2cb348e9b1` under MIT. `build.mjs` contains the reviewed Android patches: native publication loaders, script-free iframe sandboxes, legacy shadow-root styling, and an equivalent non-BigInt HUFF/CDIC bit reader. The original vendor files remain unchanged for review. All runtime code, polyfills, and licenses are bundled into the APK; Node/pnpm are development tools only.

Build with `tools/prepare-reader.ps1` from the repository root. The script uses the pinned pnpm lockfile. Directly, from this directory: `pnpm install --frozen-lockfile`, `pnpm test`, `pnpm build`.

Progress is calculated from stable section weights and DOM text offsets, not viewport/page counts. Rendering support is not considered complete until the Android host, format corpus, location restoration, security, and API 26 compatibility tests pass. The current bundle alone is not an implemented Reading feature.
