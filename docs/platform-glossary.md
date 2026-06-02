# Miyabi Nano Platform Glossary

Use these terms consistently in implementation notes, experiment records, and
conference material.

| Term | Definition |
| --- | --- |
| Android AICore | The Android system service used by ML Kit GenAI APIs to access and execute supported Gemini Nano capabilities on-device. |
| Base model | The system-managed Gemini Nano model available through AICore on a supported device. Different devices can expose different base-model versions. |
| Feature-specific asset | A downloadable asset required by a configured ML Kit GenAI capability. Do not imply that one summarizer download proves every capability is ready. |
| Provisioning | The process of making required AICore and feature-specific assets available for a configured capability. Provisioning can require network access and can be interrupted or unavailable. |
| Capability status | The runtime readiness state returned for a configured ML Kit GenAI feature, such as unavailable, downloadable, downloading, or available. |
| Unsupported device | A device or configuration where the intended capability cannot be used. Android version alone does not establish support. |
| Not-yet-ready device | A potentially supported device where required initialization or provisioning has not completed. This is distinct from an unsupported device. |
| Cold run | A measured operation performed before the relevant inference path has been warmed for the experiment session. The protocol must state whether preparation or provisioning time is included. |
| Warm run | A measured operation performed after the relevant inference path has already run under the experiment protocol. |
| Offline-before-provisioning | A device state where required capability assets are not ready and network access is unavailable. |
| Offline-after-provisioning | A validation scenario where required assets were provisioned first, connectivity is then disabled, and the configured capability is exercised on a physical device. |
| App-level observation | A value collected by the app, such as request elapsed time or JVM heap usage. It is not automatically a system-level benchmark. |
| Real-device evidence | A dated observation captured on an identified physical device with relevant configuration and protocol context. |
