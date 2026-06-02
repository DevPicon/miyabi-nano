# Miyabi Nano Evidence Priorities

Priority order matters. These demonstrations should be implemented before adding
new capabilities.

| Priority | Demonstration | Why it matters |
| ---: | --- | --- |
| 1 | Capability-specific availability and provisioning matrix | The central engineering problem is device reality, not API invocation. |
| 2 | Unsupported-device and not-yet-ready UX | A conference demo must show the honest failure path, not only the happy path on one flagship phone. |
| 3 | Cold, warm, and repeated-run latency methodology | A single timer is not benchmark evidence. |
| 4 | Offline-after-provisioning experiment | Offline-first is a core claim and needs a reproducible proof protocol. |
| 5 | Foreground loss, cancellation, rotation, and process-recreation behavior | AICore inference is foreground-only; lifecycle handling is a first-class platform constraint. |
| 6 | Typed failure-mode explorer | The platform exposes meaningful error codes that should become teachable UX states. |
| 7 | Sustained-run battery, thermal, and quota experiment | Android exposes a battery-use quota failure. This is stronger evidence than speculative power claims. |
| 8 | Experiment export with device and model metadata | Conference material requires evidence that can be compared and reviewed. |
| 9 | Privacy-mode storage policy | Local inference is not enough if raw text is silently persisted or backed up. |
| 10 | Realistic use-case boundary document | The strongest conclusion may be where OS-supported AI should not be used. |
