# Timing Milestones

Timing values are monotonic elapsed durations captured with `System.nanoTime()`.
They are app-level observations, not system traces.

| Field | Current semantic |
| --- | --- |
| `preparationWaitMs` | Time spent awaiting `prepareInferenceEngine()` |
| `downloadDurationMs` | Null for inference records because provisioning occurs outside the run |
| `firstVisibleOutputMs` | For current non-streaming APIs, elapsed time from readiness-approved request start until the complete output exists |
| `inferenceCompletionMs` | Elapsed time from readiness-approved request start until non-streaming inference completes |
| `persistenceMs` | Duration of the first Room write; the row is replaced with this value afterward |
| `userPerceivedTotalMs` | Request-to-complete-output duration. The ViewModel publishes output before awaiting persistence |

Legacy `modelLoadTimeMs`, `inferenceTimeMs`, and `totalTimeMs` remain populated
for compatibility. They now mean preparation wait, direct inference call
duration, and request-to-completion duration respectively.

Provisioning download timing requires a separate record or session event and is
not inferred from an inference request.
