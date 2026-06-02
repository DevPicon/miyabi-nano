# Miyabi Nano

An Android engineering study of OS-supported on-device AI through Android AICore
and ML Kit GenAI APIs.

`miyabi-nano` explores what application engineers still own when the platform
vendor supplies the model execution path: capability detection, provisioning UX,
lifecycle behavior, failure handling, measurement, privacy boundaries, and
realistic product limits.

This repository is not intended to mirror `iki-nano`. The portfolio lanes answer
different engineering questions:

- `iki-nano` explores iOS self-managed runtime engineering.
- `miyabi-nano` explores Android OS-supported on-device AI experimentation.

The goal is evidence-backed educational material, not feature parity and not a
wrapper-demo feature checklist.

## Documentation

Start with the [documentation guide](docs/README.md). It introduces:

- [AICore and Gemini Nano basics](docs/concepts/aicore-and-gemini-nano-basics.md)
- [OS-supported AI benefits and trade-offs](docs/concepts/os-supported-ai-benefits-and-tradeoffs.md)
- [Architecture diagrams](docs/architecture/system-flow-diagrams.md)
- [Platform responsibility model](docs/platform-responsibility-model.md)
- [Known debt register](docs/known-debt-register.md)

## Screenshots

<p align="center">
  <img src="screenshots/Screenshot_SamsungS25-01.png" alt="Main Menu Screen" height="500">
  <img src="screenshots/Screenshot_SamsungS25-02.png" alt="Summarization Screen" height="500">
</p>

## Features

- **Feature-Specific Experiments:** Exercise Summarization, Proofreading, and
  Rewriting through ML Kit GenAI APIs backed by AICore
- **Capability-Aware Setup:** Check configured-feature readiness and expose
  setup progress when required platform assets are downloadable
- **Inspectable Diagnostics:** View Nano identity, readiness, device context,
  failure detail, and the public SDK request boundary
- **Offline Experimentation:** Validate behavior after required AICore assets
  have been provisioned
- **Modern Android Architecture:** Clean Architecture with MVVM pattern
- **Material 3 Design:** Modern UI built with Jetpack Compose and Material 3

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material 3
- **Minimum SDK:** 34 (Android 14)
- **Target SDK:** 36 (Android 16)
- **Architecture:** Clean Architecture + MVVM
- **Dependency Injection:** Hilt
- **Navigation:** Navigation Compose
- **Asynchronous:** Kotlin Coroutines & Flow
- **Build System:** Gradle with Kotlin DSL
- **Annotation Processing:** KSP (Kotlin Symbol Processing)
- **ML Framework:** ML Kit GenAI APIs backed by Android AICore and Gemini Nano

## Prerequisites

- **Android Studio:** Hedgehog (2023.1.1) or later
- **JDK:** 17 or later
- **Gradle:** 8.0+ (included in wrapper)
- **Compatible Device:** Android device running Android 14 (API 34) or later with Gemini Nano support

## Device Compatibility & Limitations

### Important: Gemini Nano Availability

Gemini Nano availability is not implied by Android version alone. ML Kit
maintains an official [GenAI API device-support matrix](https://developers.google.com/ml-kit/genai#device_support).
The definitive answer for a configured feature on a specific device is the
runtime `checkFeatureStatus()` result.

ML Kit GenAI APIs rely on Android AICore. Availability can vary by device,
Gemini Nano base-model version, downloaded feature assets, language
configuration, and AICore initialization state. The official API documentation
also states that these APIs are not supported on devices with an unlocked
bootloader.

### Repository Device Observations

This table records repository observations, not a general support guarantee.

| Date | Device identity | Android version | Repository observation |
| --- | --- | --- | --- |
| 2026-06-02 | `samsung SM-F731B` | Android 16 / API 36 | Summarization, Proofreading, and Rewriting produced output after provisioning. A repeated Summarization-only generation failure recovered after uninstall and reinstall; the smallest effective recovery boundary remains under investigation. |

### Unsupported And Not-Yet-Ready Devices

The app checks each configured capability before inference. Unsupported-device
evidence remains incomplete across the broader official device matrix. See the
[implementation plan](docs/miyabi-nano-implementation-plan.md) before making
broader claims.

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd miyabi-nano
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select **File → Open**
3. Navigate to the cloned `miyabi-nano` directory
4. Click **OK**
5. Wait for Gradle sync to complete

### 3. Configure Local Properties

The project includes a `local.properties` file (not tracked in git) that contains your Android SDK path. Android Studio typically creates this automatically.

If needed, create it manually:

```properties
sdk.dir=/path/to/your/Android/sdk
```

### 4. Build the Project

```bash
./gradlew build
```

Or use Android Studio's build menu: **Build → Make Project**

### 5. Run the Application

1. Connect an Android device (API 34+) or start an emulator
2. Click the **Run** button in Android Studio (or press `Shift + F10`)
3. The app will:
   - Check bootstrap readiness and retrieve Nano identity where exposed
   - Keep capability-specific setup inside each experiment
   - Display diagnostics and recovery guidance for observable failures

## Project Structure

```
miyabi-nano/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/dev/picon/android/miyabinano/
│   │   │   │   ├── di/                    # Dependency injection modules
│   │   │   │   ├── domain/                # Business logic & use cases
│   │   │   │   ├── navigation/            # Navigation setup
│   │   │   │   ├── ui/                    # UI layer
│   │   │   │   │   ├── main/              # Main menu & model information
│   │   │   │   │   ├── inference/         # Capability-specific experiments
│   │   │   │   │   └── theme/             # App theme & styling
│   │   │   │   ├── MainActivity.kt        # App entry point
│   │   │   │   └── MiyabiNanoApplication.kt
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                          # Unit tests
│   │   └── androidTest/                   # Instrumentation tests
│   └── build.gradle.kts                   # App module build config
├── gradle/                                # Gradle wrapper files
├── build.gradle.kts                       # Root build config
├── settings.gradle.kts                    # Project settings
└── README.md
```

## How It Works

### Architecture Overview

The app follows Clean Architecture principles:

1. **UI Layer (Compose):** Declarative UI with state hoisting
2. **ViewModel Layer:** Manages UI state and business logic
3. **Domain Layer:** Contains use cases (business rules)
4. **Data Layer:** Wraps the configured ML Kit GenAI feature APIs

### Capability Flow

1. The app checks the configured feature through `checkFeatureStatus()`.
2. If required assets are missing, the experiment exposes setup and progress.
3. When ready, the app creates a configured ML Kit client and prepares the
   inference engine.
4. The app submits the public feature-API request through AICore.
5. The app closes the client and renders output or typed recovery guidance.
6. Diagnostics retain the public request boundary and runtime context without
   pretending to expose platform-managed internals.

### Model Download

The app uses ML Kit GenAI feature APIs which:
- Surfaces feature-download callbacks when configured assets are downloadable
- Relies on Android AICore for system-managed Gemini Nano access
- Provides download progress callbacks
- Exposes download failures for application-level recovery UX

## Usage

### Basic Experiment Flow

1. Open the app
2. Complete bootstrap or capability-specific setup if prompted
3. Choose an experiment
4. Load baked data or enter compatible input
5. Tap "Run Inference"
6. Inspect output, exploratory metrics, and diagnostics

### Summarization Input Notes

- Article summarization requires more than 400 characters.
- ML Kit reports best results with at least 300 words.
- ML Kit limits summarization input to fewer than 4000 tokens, approximately
  3000 English words.
- A configured capability reporting `AVAILABLE` does not guarantee successful
  output for every legal article or every transient AICore state.

### Tips

- **First Launch:** Model download can take several minutes depending on network speed
- **Offline Use:** Treat offline-after-provisioning behavior as a validation
  scenario, not an unconditional guarantee
- **Text Length:** Longer texts may take a few seconds to process
- **Privacy:** Inference uses the on-device API path, but the current experiment
  history also stores input and output text locally

## Measurement Limitations

The current metrics UI is an exploratory instrumentation scaffold, not a
benchmark report.

- **Latency:** Records exploratory timing milestones including preparation,
  completion, persistence, and user-perceived timing. Controlled cold/warm
  protocols and statistical summaries remain future work.
- **Tokens:** Uses a local word-and-punctuation heuristic. It is not the Gemini
  Nano tokenizer.
- **Memory:** Uses app-process JVM heap observations. These values are not
  system-level or model-memory measurements.
- **Evidence boundary:** Do not generalize a single app-level observation across
  devices, Gemini Nano versions, thermal states, or provisioning states.

## Building for Release

```bash
./gradlew assembleRelease
```

The APK will be generated in:
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

For signed releases, configure signing in `app/build.gradle.kts`.

## Troubleshooting

### Gradle Sync Issues

```bash
./gradlew clean build --refresh-dependencies
```

### Model Download Fails

- Ensure stable internet connection
- Check that the device has sufficient free storage for required AICore assets
- Verify device compatibility
- Check Google Play Services is updated

### App Won't Run

- Verify minimum SDK is 34 (Android 14)
- Check that device/emulator is running Android 14+
- Ensure Gradle sync completed successfully

### "Model Not Available" Message

This is normal on unsupported devices. See [Device Compatibility](#device-compatibility--limitations) section above.

## Privacy & Security

- **On-Device API Path:** Inference requests use ML Kit GenAI APIs backed by
  Android AICore
- **No Analytics:** No user data collection or tracking
- **Offline Boundary:** Offline-after-provisioning behavior requires explicit
  real-device validation
- **Local Experiment History:** The current metrics database stores input and
  output text locally; privacy controls and backup policy remain planned work

## Contributing

We welcome contributions! Here's how you can help:

1. **Report Issues:** Found a bug or have a feature request? [Open an issue](https://github.com/DevPicon/miyabi-nano/issues/new)
2. **Submit Pull Requests:**
   - Fork the repository
   - Create a feature branch (`git checkout -b feature/amazing-feature`)
   - Commit your changes (`git commit -m 'Add amazing feature'`)
   - Push to the branch (`git push origin feature/amazing-feature`)
   - Open a Pull Request

Please ensure your code follows:
- Kotlin coding conventions
- Material 3 design guidelines
- Clean Architecture principles
- Existing project structure

## License

MIT License

Copyright (c) 2024 Armando Picon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

**The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.**

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Acknowledgments

- Built with [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Uses [ML Kit GenAI APIs](https://developers.google.com/ml-kit/genai)
- Powered by [Gemini Nano through Android AICore](https://developer.android.com/ai/gemini-nano)

## Additional Resources

- [Miyabi Nano Platform Glossary](docs/platform-glossary.md)
- [Google AI on Android Documentation](https://developer.android.com/ai)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose/documentation)
- [Gemini Nano on Android](https://developer.android.com/ai/gemini-nano)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
