# Miyabi Nano Portfolio Positioning

## Purpose

`miyabi-nano` is the Android lane for studying OS-supported on-device AI. It is
not an Android port of `iki-nano`, and parity with an iOS self-managed runtime is
not the goal.

The core question is:

> What changes when the platform vendor supplies the model execution path through
> Android AICore and ML Kit GenAI APIs, while the application still owns
> capability detection, provisioning UX, lifecycle behavior, failure handling,
> measurement, and product boundaries?

The repository should become an evidence-backed engineering case study, not a
feature catalog and not a wrapper demo.

## Portfolio Strategy

- `iki-nano` explores iOS self-managed runtime engineering.
- `miyabi-nano` explores Android OS-supported on-device AI experimentation.
- The comparison should focus on responsibility boundaries and observed
  trade-offs, not feature parity.

## Current Strategic Assessment

| Dimension | Score | Assessment |
| --- | ---: | --- |
| Strategic alignment | 5/10 | The repository uses the correct Android-native path, but its narrative still reads like a summarization demo rather than an OS-supported AI engineering study. |
| Technical credibility | 2/10 | There is real ML Kit integration and an initial measurement scaffold, but the current branch does not compile and the repository lacks real-device evidence, lifecycle rigor, benchmark methodology, failure taxonomy, and meaningful tests. |

The highest-value work is not adding more GenAI APIs. It is making the platform
constraints observable, testable, and teachable.
