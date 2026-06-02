# OS-Supported On-Device AI: Benefits And Trade-Offs

## Why AICore Matters

When the operating system provides a managed foundation-model path, an Android
app does not need to ship and maintain its own large-model runtime. AICore acts
as the system boundary between the app and Gemini Nano.

Google's Android documentation describes AICore as responsible for managing
model distribution and updates while using on-device hardware acceleration.
ML Kit documentation also states that apps can use the shared Gemini Nano model
already present on the device.

## Benefits

| Benefit | Why It Matters |
| --- | --- |
| Shared base model | Multiple apps can use the system-managed Gemini Nano model instead of packaging separate copies. This can reduce redundant storage and model-download waiting when the model is already present. |
| Easier deployment | The app does not distribute, update, or directly load a large foundation-model file. |
| Hardware acceleration | AICore uses supported device hardware without requiring app-specific accelerator integration. |
| Local processing | Input, inference, and output remain on-device through the feature API path, avoiding a required server round trip. |
| Offline-capable experiences | Prepared capabilities can remain useful without reliable connectivity. This repository still validates offline-after-provisioning explicitly rather than claiming unconditional offline behavior. |
| Lower per-request server cost | On-device inference avoids a hosted inference call for each feature request. |
| Platform-managed safety boundary | AICore manages request processing, model weights, and safety mechanisms inside the OS-supported path. |

## What The App Still Owns

OS support changes the engineering problem; it does not eliminate it.

| Application responsibility | Why It Remains Necessary |
| --- | --- |
| Capability detection | Device model, Android version, Nano version, language, and feature assets can affect availability. |
| Provisioning UX | Required assets may still need download time, network access, storage, and retry handling. |
| Foreground lifecycle | ML Kit GenAI inference is permitted only while the app is the top foreground app. |
| Failure handling | AICore can report busy service, quota, disk pressure, unsupported configuration, processing failures, and transient disconnects. |
| Privacy policy | Local inference does not excuse silent local payload retention or unsafe backup rules. |
| Evidence quality | Battery, thermal, latency, and offline claims require physical-device protocols and reproducible context. |

## When This Approach Fits

Use an OS-supported path when:

- the platform feature API matches the product use case
- supported-device scope is acceptable
- local processing, offline-capable behavior, and lower server dependency are
  valuable
- platform-managed updates and hardware selection are preferable to owning a
  custom runtime

## When It May Not Fit

Consider another approach when:

- the product must support devices outside the official matrix
- the product needs deterministic runtime control or a specific model version
- background execution is required
- the feature API cannot express the required task
- quotas, input limits, or beta API maturity are unacceptable

## Portfolio Contrast

`miyabi-nano` deliberately differs from `iki-nano`:

| Repository | Engineering question |
| --- | --- |
| `iki-nano` | What must an iOS app own when it manages the runtime itself? |
| `miyabi-nano` | What remains difficult when Android provides an OS-supported AI path? |

Neither path is universally superior. They expose different responsibility
boundaries and failure modes.

## Official References

- [Gemini Nano on Android](https://developer.android.com/ai/gemini-nano)
- [ML Kit GenAI API overview](https://developers.google.com/ml-kit/genai)
