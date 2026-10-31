# Reading formats and storage

This describes the v3 implementation. The v3 validation record remains in progress until the document corpus and device checks are completed.

| Source | Offline path | Practical limits |
| --- | --- | --- |
| EPUB 2/3 | Foliate JS, with native ZIP resource access | DRM-free; reflowable and fixed-layout publications. Script execution is disabled in publication frames. Each individual resource is limited to 32 MB. |
| PDF | Native PDFium page rendering and text search | Actual PDF page numbers, zoom and accessible page text. Image-only scans need external OCR; password-protected PDFs need an unlocked copy. |
| MOBI, AZW, AZW3 | Foliate's PalmDOC/MOBI/KF8 reader, native file ranges | Only recognized, DRM-free BOOKMOBI containers. KFX and other files sharing an Amazon extension are not MOBI. Text payloads are limited to 16 MB. |
| FB2 | Foliate for small publications; streaming text normalization for large files | Direct files up to 16 MB retain supported illustrations. Large-file normalization preserves text; the retained original contains the illustrations. |
| FB2 ZIP | Native bounded extraction, then the FB2 path | One FB2 publication per archive; multiple-book bundles must be imported separately. |
| TXT | Streaming, chaptered EPUB conversion | UTF-8 and UTF-16 BOM detection. Original bytes remain available privately. |
| HTML/XHTML | Offline EPUB normalization | Preserves basic headings, lists, tables and emphasis, with BOM/meta charset detection. Scripts/styles/forms are removed; image alt text is retained. Companion folders and remote resources are not fetched. Maximum 16 MB source. |
| RTF | Streaming text extraction to a chaptered EPUB | Text is preserved; RTF layout, embedded objects, and illustrations are not rendered. |
| CBZ | Native ZIP/image decoding | Numeric filename ordering, actual image page numbers; no OCR. Individual images are limited to 32 MB and are downsampled for display. |
| CBR | libarchive RAR/RAR5 → private ZIP copy, then native images | Single-volume, unencrypted archives supported by libarchive. Proprietary RAR features and damaged archives may fail with a readable error. |
| KFX, DRM/encrypted e-books, DjVu, LIT, LRF, arbitrary Palm/PDB containers | Unsupported | No DRM removal, proprietary conversion service, network converter, or GPL reader engine is bundled. Import a DRM-free EPUB/PDF equivalent. |

All imports are limited to 2 GB, at most 50,000 ZIP/archive entries, and at most 2 GB total expanded archive data. A large publication is not loaded as a complete ZIP byte array. Reflowable chapters and resources still need to fit the per-resource limit. A single unchaptered, very large XML/HTML resource may need to be split before import.

## Import and privacy

Use Reading → Import or Android's Open with menu for registered formats. The app copies the content URI to private storage, calculates SHA-256 while copying, validates the publication, and creates a reading copy when conversion is needed. Conversion completes in a staging file before publication. Imports can be cancelled. The source is never edited.

Identical bytes reuse the existing library item and its saved position, bookmarks, and history. Moving, deleting, or revoking access to the source does not affect the private copy. Reimporting the same bytes can repair a missing private source. Interrupted imports can be retried by selecting the file again. A removed library item can be restored through Undo; its private copy is retained for recovery.

Startup cleans recognized incomplete staging files after an interrupted import. A fully copied original is retained even if the process stopped before its database record was committed; reimporting the same bytes can reuse it. Plain TXT supports UTF-8 and UTF-16 BOMs; other legacy text encodings should be converted to UTF-8 before import.

The APK bundles all reader JavaScript, fonts/polyfills needed by its runtime, and licenses. It runs no local HTTP server and has no INTERNET permission. WebView file/content access and external-resource requests are denied. Only the trusted main frame receives a native message port; publication frames are sandboxed without scripts.

## Positions and reading time

PDFs and comics store actual zero-based page indices, zoom, and normalized offsets. The interface presents one-based page numbers. Reflowable books store a versioned stable locator, a short quote, chapter information, and logical progress derived from document text offsets and section weights. Viewport dimensions and font size do not define reflowable progress. A bookmark stores its own position snapshot.

Fixed-layout EPUBs preserve their page design and provide fit-page, fit-width, and 100/150/200% zoom. Font controls apply only to reflowable publications. PDF/comic rendering is bounded to six million pixels per displayed bitmap; zoom enlarges that bounded image rather than allocating an unlimited-resolution bitmap.

Finishing a book is an explicit status change. Reaching the last visible page does not silently mark it finished. Want to read, Reading, Paused, and Finished remain separate from the last saved location.

Reading time uses monotonic clock intervals while the reader is visible and resumed. Five-second checkpoints bound loss after process death. Backgrounding pauses time, and two minutes without interaction stops further credit until another page turn, tap, or control interaction. Historical sessions retain their local date and time-zone ID. Daily/weekly goals are optional and do not block reading.

## Dependencies

- [Foliate JS](https://github.com/johnfactotum/foliate-js), MIT, pinned commit `78914aef4466eb960965702401634c2cb348e9b1`. Original vendor files are retained; Android compatibility/security patches are explicit in `reader/build.mjs`.
- [PdfiumAndroidKt](https://github.com/johngray1965/PdfiumAndroidKt), Apache 2.0 bindings and PDFium's permissive third-party notices, Maven version 2.0.3.
- [libarchive](https://github.com/libarchive/libarchive), BSD, pinned version 3.8.10 / commit `0c48412be2d94afe1859a807ea7896a18d8900bd`. The app enables only RAR/RAR5 readers at runtime and ZIP writing for normalization.
- rtfparserkit 1.16.0 (Apache 2.0), jsoup 1.18.3 (MIT), fflate 0.8.2 (MIT), core-js 3.46.0 (MIT), ResizeObserver polyfill 1.5.1 (MIT).

Node, pnpm, Git, and the Android build toolchain are development dependencies. They are not needed on the phone.
