# Capability Preparation Contract

## Purpose

`CapabilityPreparationClient` is the application-owned boundary for one
configured ML Kit GenAI capability. It keeps ML Kit callbacks and future types
out of UI state and orchestration code.

## Responsibilities

Each client exposes:

- configured capability identity
- runtime readiness check
- provisioning events
- inference-engine preparation
- Gemini Nano base-model lookup
- inference execution
- explicit resource release

## Capability Configurations

The repository currently needs one configured client for each visible
capability:

| Capability | ML Kit client configuration |
| --- | --- |
| Summarization | Article input, one-bullet output, English |
| Proofreading | Keyboard input, English |
| Rewrite formal | Professional output, English |
| Rewrite casual | Friendly output, English |
| Rewrite concise | Shorten output, English |

These configurations share an application contract but do not imply shared
readiness. Each configured client must report its own runtime state.

## Follow-Up Boundaries

- `TASK-11` owns the per-capability readiness state machine.
- `TASK-12` owns readiness presentation on the main menu.
- `TASK-13` owns migration of inference orchestration onto this contract.
- `TASK-16` owns the final resource-ownership policy.
