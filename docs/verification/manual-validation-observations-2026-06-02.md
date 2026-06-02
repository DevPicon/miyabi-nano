# Manual Validation Observations: 2026-06-02

These observations were reported from Android Studio physical-device sessions
on an attached Samsung `SM-F731B`. ADB reported Android API `36` and build
`BP2A.250605.031.A3.F731BXXS6FZC5`. They are useful evidence inputs, but they
are not a complete device matrix.

## Observed

- Fresh launches and repeated installs completed without a reported Room
  migration crash.
- The home screen displayed five text experiments and a Gemini Nano model-info
  card.
- Each experiment kept inference disabled until input was present. Loading
  baked data, clearing input, running inference, displaying output, and
  displaying exploratory metrics worked during the reported session.
- The earlier provisioning `1-DOWNLOAD_ERROR / 6-IPC_ERROR` did not recur.
- Backgrounding the app during summarization interrupted inference. The UI
  displayed: `AICore could not process this request or response. Adjust the
  input and retry.`
- Opening the in-app diagnostics modal while summarization was processing was
  followed by repeated failures on later attempts using the same input. The
  retained SDK detail was: `Couldn't generate a response. Try a different
  input.`
- Proofreading and rewriting continued to work while summarization repeatedly
  returned the response-generation failure, including with a different baked
  summarization fixture. This narrows the observation to the configured
  summarization API path rather than Gemini Nano availability in general.
- The revised recommended-range summarization article continued to fail. The
  repository now treats this as a summarization-specific negative result under
  investigation rather than a missing Gemini Nano model.

## Interpretation

- The background-interruption message is misleading because the input was
  valid before foreground loss. `TASK-28` owns correction and revalidation.
- The retained SDK detail does not prove foreground loss or a poisoned client.
  Repeating the same input can legitimately repeat a response-generation
  failure. `TASK-29` owns controlled comparison using the same input, different
  input, navigation away and back, and app relaunch.
- Official ML Kit documentation requires `ARTICLE` input to exceed 400
  characters and states that the model performs best with at least 300 words.
  Existing baked summarization probes were mostly valid but below that
  recommended range; one was below the hard minimum. `TASK-54` owns input
  boundary enforcement and stronger fixtures.
- ML Kit feature-specific summarization does not expose a developer-authored
  prompt. The public boundary is `SummarizationRequest.builder(text).build()`
  plus `SummarizerOptions`. `TASK-55` exposes the exact app-submitted SDK
  request snapshot without inventing an internal prompt.
- Successful inference context is persisted but not inspectable through a
  secondary UI surface. `TASK-51` owns a diagnostics modal.
- The home model card is the key platform signal and should appear before
  experiment navigation. Insets should be handled consistently. `TASK-52` owns
  the layout correction.

## Still Unknown

- Whether every reported session ran on the attached Samsung `SM-F731B`.
- Whether the diagnostics modal affected inference or merely coincided with a
  repeatable response-generation failure for that input.
- Unsupported-device UX.
- Offline-after-provisioning behavior.
- Physical-device battery, thermal, and quota behavior.
