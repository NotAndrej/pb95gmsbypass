# Progressbar 95 GMS bypass

A simple Xposed module that bypasses Progressbar95's Google Mobile Services (GMS) requirement on devices without GMS.

This module does not include or modify the game APK. It only bypasses the GMS availability check at runtime.

## Requirements

- Rooted Android device + [LSPosed](https://github.com/JingMatrix/Vector/releases/) (or [LSpatch](https://github.com/LSPosed/LSPatch/releases) for non-root users) installed.
- Android Studio / Gradle to build.

## Installation

1. Build or download the APK from the releases page.
2. Install it.
3. Enable it in LSPosed or LSPatch.
4. Scope it to Progressbar95.
5. Force-stop and relaunch the game.

## Building

For debug builds:
```bash
./gradlew assembleDebug
```

For release builds:
```bash
./gradlew assembleRelease
```

The output APK will be under `app/build/outputs/apk/release/` for a **release** build, and `app/build/outputs/apk/debug/` for a **debug** build.

## Known limitations

- This only fakes the availability *check*. If the game later makes real
  Play Games Services calls (achievements/cloud save) that depend on GMS
  actually functioning, those may still fail silently. Installing microG
  is the more complete fix for that part.

- The IDLE network feature does not work. It hangs and eventually reports "No internet connection".

## License

This project is licensed under the [GNU GPL v3](LICENSE).
