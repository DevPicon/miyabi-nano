# Miyabi Nano Known Debt Register

This register records known gaps explicitly. A listed gap is not evidence that
the behavior has been implemented, verified, or reviewed.

| Gap | Why It Matters | Owning Task |
| --- | --- | --- |
| The home screen exposes a per-capability download matrix rather than a concise bootstrap summary. | The UI overemphasizes manual downloads and obscures the distinction between shared Gemini Nano identity and capability-specific assets. | `TASK-46`, `TASK-47` |
| Current peak-memory reporting exposes the runtime maximum heap limit. | The UI can misrepresent a heap limit as observed model or system-memory behavior. | `TASK-21` |
| Experiment records lack reproducibility context and structured export. | Results cannot be compared credibly across devices, configurations, or runs. | `TASK-18`, `TASK-19`, `TASK-22` |
| Offline-after-provisioning behavior is claimed but not proven through a real-device protocol. | Offline-first credibility requires dated evidence, not inference from API documentation. | `TASK-25`, `TASK-30` |
| Raw inference input and output text are stored locally by default. | On-device inference alone does not establish a privacy-preserving storage policy. | `TASK-37` |
| Backup configuration remains a template while local experiment text is stored. | Sensitive experiment history may be eligible for implicit backup. | `TASK-38` |
| Android instrumentation coverage remains generated placeholder evidence. | Android-specific lifecycle claims are not yet protected by realistic instrumentation. | `TASK-30` |
| Device-support evidence is incomplete. | An attached device identity is not proof of configured-feature readiness or behavior. | `TASK-30`, `TASK-39` |
| Gemini Nano base-model identity is surfaced but not persisted in experiment records. | Output variability across Nano versions cannot be reproduced credibly without retained `getBaseModelName()` evidence. | `TASK-18`, `TASK-19` |
| Image Description, Speech Recognition, and Prompt APIs are not represented. | These APIs are educationally relevant but have distinct maturity levels, inputs, and support matrices. Adding them prematurely would hide rather than teach platform constraints. | `TASK-48`, `TASK-49`, `TASK-50` |
