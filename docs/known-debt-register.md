# Miyabi Nano Known Debt Register

This register records known gaps explicitly. A listed gap is not evidence that
the behavior has been implemented, verified, or reviewed.

| Gap | Why It Matters | Owning Task |
| --- | --- | --- |
| Successful experiment records capture reproducibility context but are not exportable yet. Failed and blocked runs are not persisted. | Results cannot be analyzed credibly outside the app or compared across all outcome categories until export and failed-run retention are implemented. | `TASK-22` |
| Offline-after-provisioning behavior is claimed but not proven through a real-device protocol. | Offline-first credibility requires dated evidence, not inference from API documentation. | `TASK-25`, `TASK-30` |
| Raw inference input and output text are stored locally by default. | On-device inference alone does not establish a privacy-preserving storage policy. | `TASK-37` |
| Backup configuration remains a template while local experiment text is stored. | Sensitive experiment history may be eligible for implicit backup. | `TASK-38` |
| Android instrumentation coverage remains generated placeholder evidence. | Android-specific lifecycle claims are not yet protected by realistic instrumentation. | `TASK-30` |
| Device-support evidence is incomplete. | An attached device identity is not proof of configured-feature readiness or behavior. | `TASK-30`, `TASK-39` |
| Image Description and Prompt APIs are not represented. | These API families have distinct maturity levels, inputs, and support matrices. Adding them prematurely would hide rather than teach platform constraints. | `TASK-48`, `TASK-50` |
