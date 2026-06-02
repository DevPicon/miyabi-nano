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

## Interpretation

- The background-interruption message is misleading because the input was
  valid before foreground loss. `TASK-28` owns correction and revalidation.
- Successful inference context is persisted but not inspectable through a
  secondary UI surface. `TASK-51` owns a diagnostics modal.
- The home model card is the key platform signal and should appear before
  experiment navigation. Insets should be handled consistently. `TASK-52` owns
  the layout correction.

## Still Unknown

- Whether every reported session ran on the attached Samsung `SM-F731B`.
- The raw SDK exception detail emitted during the observed foreground-loss
  interruption.
- Unsupported-device UX.
- Offline-after-provisioning behavior.
- Physical-device battery, thermal, and quota behavior.
