# Miyabi Nano Repository Evaluation

## Executive Assessment

The project has a credible seed: it integrates ML Kit GenAI summarization,
proofreading, and rewriting clients backed by Gemini Nano, checks summarizer
availability on launch, exposes model-download progress, persists inference
records with Room, and includes reusable test inputs.

It is not yet conference-ready. The current visible experience overstates what
has been proven. The app displays five capability entry points, but the main
download/status surface checks only the summarizer. The generic inference path
calls clients directly without capability-specific preparation. The README makes
strong offline and privacy claims without a reproducible validation protocol.
The metrics card presents rough timings and process-heap deltas as performance
evidence without benchmark controls.

Baseline verification also fails at `:app:compileDebugKotlin`. The generic
inference path uses incompatible await/result handling for the ML Kit client
return types, and `InferenceViewModel` is missing the `TestCase` model import.
Restoring a compiling baseline is the first implementation prerequisite.

## What Is Technically Serious

- `GenAiModule` creates real ML Kit GenAI clients for summarization,
  proofreading, and three rewriting configurations. This is the correct
  high-level Android OS-supported lane.
- `ModelDownloadViewModel` maps `FeatureStatus` values and exposes provisioning
  progress for the summarizer.
- `InferenceUseCase` routes five capability variants and emits initial latency,
  token-estimate, character-count, and process-memory observations.
- Room persistence provides a foundation for experiment history instead of
  screenshot-only demonstrations.
- `TestDataRepository` supplies reusable input fixtures, which is the beginning
  of repeatable experiments.

## What Is Superficial Or Misleading

- The current `main` branch does not compile. Multi-capability work was committed
  without a successful baseline build.
- The main screen presents five capabilities as equally ready, while model
  status and download lifecycle are checked only through the summarizer.
- The active generic inference path does not call `checkFeatureStatus()` before
  inference and does not explain adapter downloads, transient provisioning, or
  unsupported configurations.
- The old summarization flow and the new generic flow coexist. The old flow is
  still wired in navigation but is no longer reachable from the visible main
  menu.
- `InferenceUseCase` measures elapsed wall-clock time around one request and
  labels it inference latency. It does not distinguish cold preparation, warm
  inference, queueing, download wait, or UI-perceived time.
- `MemoryTracker.getPeakMemoryMB()` returns the runtime maximum heap limit, not
  observed peak memory.
- Token counts are heuristic estimates. They are not model tokenizer results.
- Raw input and generated output are persisted to Room. This weakens the privacy
  story unless storage becomes opt-in, controllable, and excluded from backup.
- Backup is enabled while backup-rule files remain template placeholders.
- The README is stale and overconfident. It includes a December 2024 device list
  and absolute phrases such as `100% offline`.
- Unit and instrumentation tests are generated placeholders.

## Narrative Support Matrix

| Narrative claim | Current support | Evidence gap |
| --- | --- | --- |
| Android with OS-supported AI | Partial | The integration uses ML Kit GenAI APIs, but the docs do not clearly explain the AICore boundary or what Android owns versus what the app owns. |
| Practical Gemini Nano / AICore experimentation | Partial | Real clients exist, but experiments are not versioned, exported, or reproducible. |
| Device support reality | Weak | Only summarizer status is shown. No capability matrix, device fingerprint, AICore compatibility explanation, or unsupported-device evidence exists. |
| Latency and UX implications | Weak | A single elapsed time is recorded. There is no cold/warm methodology, streaming comparison, distribution reporting, or user-perceived timing. |
| Battery and thermal implications | Missing | No battery, thermal, sustained-run, or quota experiment exists. |
| Offline behavior after provisioning | Claimed, not demonstrated | No explicit offline validation workflow or evidence artifact exists. |
| Platform constraints and failure modes | Weak | Exceptions are reduced to generic messages. |
| Offline-first UX | Weak | The UI exposes a download button, but does not model interrupted downloads, retry semantics, or per-capability readiness. |
| Privacy-preserving AI | Weak | Inference is on-device, but raw experiment text is persisted and backup remains enabled without exclusions. |
