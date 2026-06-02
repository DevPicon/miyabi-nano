# Miyabi Nano Repository Evaluation

## Executive Assessment

The project has a credible seed: it integrates ML Kit GenAI summarization,
proofreading, and rewriting clients backed by Gemini Nano, checks summarizer
availability on launch, exposes model-download progress, persists inference
records with Room, and includes reusable test inputs.

It is not yet conference-ready. The implementation now separates bootstrap
status from experiment-specific readiness, gates inference by capability, and
records versioned reproducibility context and timing milestones. The remaining
gap is evidence: supported-device, unsupported-device, offline, interruption,
battery, and thermal behavior still require reproducible physical-device
validation. The README also makes strong offline and privacy claims without a
completed validation protocol.

The original baseline failed at `:app:compileDebugKotlin`. That prerequisite has
been restored, but physical-device validation remains deferred.

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

- The baseline now compiles, but runtime validation remains incomplete across
  supported and unsupported physical devices.
- The main screen now exposes a concise bootstrap summary and moves
  capability-specific setup into each experiment. Physical-device UX evidence
  is still missing.
- The active generic inference path checks readiness before inference and maps
  typed failure outcomes. The full device-specific failure matrix is still
  unverified.
- The active inference path is unified behind an app-owned capability boundary
  with explicit closeable-client ownership. Lifecycle interruption still needs
  physical-device validation.
- `InferenceUseCase` now records preparation, inference, completion,
  persistence, and user-perceived timing milestones. Benchmark distributions
  and controlled cold/warm protocols remain future work.
- Process memory observations are now labeled as process-heap delta and runtime
  maximum heap rather than observed peak model or system memory.
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
| Device support reality | Partial | Bootstrap and capability-specific readiness are modeled, but unsupported-device and physical-device evidence remain missing. |
| Latency and UX implications | Partial | Timing milestones are recorded, but cold/warm methodology, distributions, and physical-device observations remain missing. |
| Battery and thermal implications | Missing | No battery, thermal, sustained-run, or quota experiment exists. |
| Offline behavior after provisioning | Claimed, not demonstrated | No explicit offline validation workflow or evidence artifact exists. |
| Platform constraints and failure modes | Partial | Typed outcomes and retry guidance exist, but the device-specific failure matrix is not validated. |
| Offline-first UX | Partial | Bootstrap and experiment setup states exist, but interruption and offline evidence remain deferred. |
| Privacy-preserving AI | Weak | Inference is on-device, but raw experiment text is persisted and backup remains enabled without exclusions. |
