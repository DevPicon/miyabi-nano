# Experiment Schema v1

Experiment records use `ExperimentSchema.CURRENT_VERSION = 1`. Schema versioning
is independent from the Room database version.

## Context Fields

| Field | Purpose | Population owner |
| --- | --- | --- |
| `appVersion` | Identify the app build | `TASK-19` |
| `deviceManufacturer`, `deviceModel`, `androidBuild`, `apiLevel` | Identify the physical Android context | `TASK-19` |
| `baseModelName` | Record the observed Gemini Nano identity where exposed | `TASK-19` |
| `featureStatusBeforeRun` | Separate ready, provisioning, and unavailable runs | `TASK-19` |
| `connectivity`, `powerState`, `thermalStatus` | Record runtime conditions | `TASK-19`, `TASK-32` |
| `runSequence`, `runClassification` | Separate ordered cold, warm, and provisioning runs | `TASK-19`, `TASK-23` |
| `fixtureId`, `heuristicInputSize` | Identify controlled or arbitrary input boundaries | `TASK-24` |
| `outcomeCategory` | Record success or stable app-owned failure categories | `TASK-19` |

## Timing Fields

The schema reserves nullable milestones for preparation wait, download duration,
first visible output, inference completion, persistence, and total
user-perceived duration. `TASK-20` owns collection semantics. A null value means
not captured, not zero.

## Migration Boundary

Room migration `1 -> 2` adds nullable or explicit-default columns. Existing
exploratory rows remain readable and carry `UNKNOWN` context where evidence was
not previously captured.
