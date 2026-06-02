# Deferred Manual Validation

Run this checklist on unlocked physical devices after the autonomous
implementation batch. Record device model, Android build, app commit, date, and
connectivity state for each observation.

## Installation And Migration

- Install the latest debug APK over the previous build without clearing app
  data.
- Launch the app and confirm Room migration `1 -> 2` does not crash.
- Confirm previously stored exploratory metrics remain readable where the UI
  exposes them.

## Bootstrap Home UX

- Confirm the home screen shows one bootstrap summary rather than five download
  rows.
- Exercise checking, initial setup, progress, available, unavailable, and retry
  states where reproducible.
- Confirm the Gemini Nano identity label transitions from not ready to the
  `getBaseModelName()` value after bootstrap setup.
- Record the exact model name returned on each tested device.

## Experiment-Level Setup

- Open summarization, proofreading, and each rewriting configuration.
- Confirm input, baked fixtures, and inference remain disabled until that
  configured capability reports available.
- Exercise capability-local setup and retry.
- Confirm editing a loaded baked fixture removes its fixture attribution in
  stored evidence.

## Failure Recovery

- If `1-DOWNLOAD_ERROR / 6-IPC_ERROR` recurs, confirm the UI describes a
  transient AICore disconnect and offers retry rather than claiming unsupported
  hardware.
- Background the app during inference and record the observed foreground
  restriction behavior.
- Record any disk-pressure, busy-service, quota, or policy outcome without
  attempting to force unsafe device conditions.

## Reproducibility Context

- Complete one inference run and inspect the stored record.
- Confirm app version, manufacturer, model, Android build, API level,
  connectivity, power annotation, thermal status, run sequence, readiness,
  Gemini Nano identity where exposed, fixture ID where selected, and input-size
  observation.
- Compare Wi-Fi, offline, and unvalidated-network annotations where practical.

## Timing Observations

- Confirm output becomes visible before persistence completes.
- Inspect preparation wait, inference duration, completion, persistence, and
  user-perceived total fields.
- Confirm download duration remains null for inference records rather than
  borrowing inference latency.

## Known Deferred Work

- Structured JSON and CSV export remains `TASK-22`.
- Android instrumentation and migration automation remain `TASK-30`.
- Image Description remains `TASK-48`.
- Prompt Alpha remains `TASK-50`.
- Speech recognition remains intentionally outside the Gemini Nano registry.
