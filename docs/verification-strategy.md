# Miyabi Nano Verification Strategy

## Verification Matrix

| Area | Automated verification | Real-device verification | Evidence artifact |
| --- | --- | --- | --- |
| Capability readiness | Unit tests for state machine | Supported and unsupported device runs | Capability matrix export and screenshots |
| Provisioning | Unit tests for callbacks and retries | Fresh or reset provisioning, interrupted network | Provisioning timeline export |
| Offline behavior | Instrumented UI checks where deterministic | Airplane-mode protocol after provisioning | Offline validation report |
| Latency | Schema and calculation tests | Controlled cold/warm repetitions | JSON/CSV exports and benchmark summary |
| Lifecycle | ViewModel and UI tests | Rotation, background, navigation, relaunch | Lifecycle scenario report |
| Failures | Error-code mapping tests | Trigger feasible platform failures | Failure-mode catalog |
| Battery and thermal | Calculation tests | Sustained-run protocol with Android tooling | Raw captures and scoped report |
| Privacy | Backup-rule and repository tests | Delete/export behavior | Privacy checklist |

## Evidence Rules

`AGENT.md` is the governance source of truth. In particular:

- Do not treat emulator-only behavior as proof for AICore- or device-dependent
  claims.
- Do not publish performance numbers without protocol, device context, sample
  count, and raw evidence.
- State the verified boundary for offline, privacy, support, and benchmark
  claims.
- Treat negative results as valuable evidence.
