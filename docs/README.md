# Miyabi Nano Documentation Guide

`miyabi-nano` is an Android engineering study of OS-supported on-device AI.
Start here if you are new to AICore, Gemini Nano, or ML Kit GenAI APIs.

## Recommended Reading Order

1. [Core concepts](concepts/aicore-and-gemini-nano-basics.md)
2. [Benefits and trade-offs](concepts/os-supported-ai-benefits-and-tradeoffs.md)
3. [Architecture diagrams](architecture/system-flow-diagrams.md)
4. [Platform responsibility model](platform-responsibility-model.md)
5. [Platform glossary](platform-glossary.md)
6. [Known debt register](known-debt-register.md)
7. [Implementation plan](miyabi-nano-implementation-plan.md)

## What This Repository Is Trying To Teach

The operating system can own model distribution, hardware acceleration, model
updates, and an isolated execution service. That removes a large amount of
application runtime engineering.

It does not remove application responsibility. The app must still detect
capability readiness, explain provisioning, retain useful diagnostics, handle
foreground restrictions, test offline-after-provisioning behavior, and scope
claims to dated physical-device evidence.

## Evidence Posture

Treat positive and negative results as equally useful:

- A capability reporting `AVAILABLE` is evidence of configured-feature
  readiness. It is not proof that every legal input will generate output.
- A working feature on one phone is not a universal device-support claim.
- On-device inference improves the privacy boundary, but local payload
  persistence and backup policy still matter.
- App-level timings are observations until a controlled protocol and raw
  evidence make them defensible benchmarks.
