# Daylight Hub for Android

An offline-first personal hub for Android phones and tablets. The first version includes a local planner, editable notes, a chat screen connected to a local OpenAI-compatible model server, and appearance settings.

## Open and build

1. Install the current stable Android Studio. Its Gradle JDK must be JDK 17 or newer.
2. Open this folder in Android Studio and let Gradle sync. If Android Studio asks which Gradle version to use, choose **9.6.0**.
3. Use **Build → Generate App Bundles or APKs → Generate APK(s)**, or run the `app` configuration on an Android 8.0+ device.

The generated debug APK is under `app/build/outputs/apk/debug/`.

This project is configured with Android Gradle Plugin 9.5.0 and Gradle 9.6.0, which are a supported pair. It uses Android API 37 for compilation and targets API 37. During the first sync, Android Studio downloads the required Gradle distribution, Android SDK, and libraries. Keep the Gradle JDK set to a supported JDK 17 or newer.

## Local AI connection

The app does not contain model weights. Choose a small, quantized model and run it with an OpenAI-compatible local server such as llama.cpp's server. The app sends requests to the endpoint set in **Settings → Local AI**. The default is `http://127.0.0.1:8080/v1`.

- For a model server running on the same Android device, enter its loopback address.
- For a model server running on a computer on the same private network, enter its private IPv4 address (for example `http://192.168.1.20:8080/v1`).
- Enter the model identifier expected by that server.

Only loopback, emulator host `10.0.2.2`, and private IPv4 ranges are accepted by the chat client. Hostnames and public internet hosts are rejected. The app makes no cloud AI calls. The app needs Android's network permission so it can contact a local server; requests are directed only to the configured local/private endpoint. To use it with no network at all, run the model server on the same device.

The assistant exposes four structured actions: create a note, create a planner item, list notes, and list planner items. All other reading, editing, and deleting can be done directly in the app. The app asks the model to clarify ambiguous dates. Model quality and tool-call support vary by model; use a model that supports OpenAI-style tool calling.

## Included

- Room database on the device for notes and planner items
- Current stable Android toolchain and AndroidX libraries (AGP 9.5, API 37, Compose BOM 2026.09)
- Create, edit, complete, and delete planner items
- Create, edit, and delete notes
- Tablet navigation rail and phone bottom navigation
- Four accent colors and compact list setting
- No analytics, ads, account, sync, or cloud service

## Project structure

- `data/`: local Room entities, DAOs, and database
- `ai/`: local model HTTP client and constrained tool-call interface
- `MainActivity.kt`: feature navigation and Compose screens

The section enum and individual screen composables provide a simple place to add future hub features.
