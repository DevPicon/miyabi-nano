# Miyabi Nano Known Debt Register

This register records known gaps explicitly. A listed gap is not evidence that
the behavior has been implemented, verified, or reviewed.

| Gap | Why It Matters | Owning Task |
| --- | --- | --- |
| The home screen exposes a per-capability download matrix rather than a concise bootstrap summary. | The UI overemphasizes manual downloads and obscures the distinction between shared Gemini Nano identity and capability-specific assets. | `TASK-46`, `TASK-47` |
| The repository contains an older summarization flow and a newer generic inference flow. | Duplicate orchestration obscures lifecycle ownership and failure behavior. | `TASK-13` |
| Inference entry points are not gated by per-capability readiness. | Unsupported or not-yet-ready devices can enter ambiguous failure states. | `TASK-14` |
| AICore failures are reduced to generic messages. | Disk pressure, foreground blocking, quotas, busy service, policy rejection, and incompatibility are not teachable or recoverable states. | `TASK-15` |
| ML Kit client resource ownership is not explicit. | Singleton clients and close behavior can create reuse or lifecycle hazards. | `TASK-16` |
| Current peak-memory reporting exposes the runtime maximum heap limit. | The UI can misrepresent a heap limit as observed model or system-memory behavior. | `TASK-21` |
| Experiment records lack reproducibility context and structured export. | Results cannot be compared credibly across devices, configurations, or runs. | `TASK-18`, `TASK-19`, `TASK-22` |
| Offline-after-provisioning behavior is claimed but not proven through a real-device protocol. | Offline-first credibility requires dated evidence, not inference from API documentation. | `TASK-25`, `TASK-30` |
| Raw inference input and output text are stored locally by default. | On-device inference alone does not establish a privacy-preserving storage policy. | `TASK-37` |
| Backup configuration remains a template while local experiment text is stored. | Sensitive experiment history may be eligible for implicit backup. | `TASK-38` |
| Unit-test coverage is minimal and the instrumentation test remains generated placeholder evidence. | Lifecycle and Android-specific claims are not protected by realistic automation. | `TASK-17`, `TASK-30` |
| Device-support evidence is incomplete. | An attached device identity is not proof of configured-feature readiness or behavior. | `TASK-30`, `TASK-39` |
| Gemini Nano base-model identity is not surfaced or persisted. | Output variability across Nano versions cannot be explained or reproduced credibly without `getBaseModelName()` evidence. | `TASK-46`, `TASK-18`, `TASK-19` |
| Image Description, Speech Recognition, and Prompt APIs are not represented. | These APIs are educationally relevant but have distinct maturity levels, inputs, and support matrices. Adding them prematurely would hide rather than teach platform constraints. | `TASK-48`, `TASK-49`, `TASK-50` |
