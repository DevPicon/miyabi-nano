# TASK-15 Completion Summary

## Task

Map typed AICore failures.

## Changed Behavior

- Expanded the app-owned failure taxonomy for incompatibility, system update,
  unavailable feature, disk pressure, busy service, cancellation, foreground
  blocking, battery quota, input bounds, generic processing failure, observed IPC
  disconnect, and unknown failures.
- Mapped ML Kit `GenAiException` values inside the adapter layer before they
  cross into domain and UI code.
- Applied adapter failure mapping consistently to readiness checks,
  provisioning, engine preparation, model-name retrieval, and inference.
- Preserved coroutine cancellation rather than converting it into a platform
  failure.
- Changed inference errors to carry structured app-owned failures.
- Added a documented taxonomy and focused tests for direct, wrapped,
  current-public, and forward-compatible documented error codes.

## Verification

- Ran `git diff --check`.
- Result: no whitespace errors.
- Searched production and test sources for raw exception-message handling.
- Ran `./gradlew testDebugUnitTest assembleDebug`.
- Result: `BUILD SUCCESSFUL`.
- Manual physical-device failure-mode validation is deferred until the end of
  the autonomous implementation run.

## Commit

`d2c348b TASK-15 map typed AICore failures`

## Known Limitations

- Experiment persistence does not yet store the structured category; that
  remains part of the experiment-schema work in `TASK-18`.
- Some failure modes are external-state dependent and require dated
  physical-device observations.
- The installed `1.0.0-beta1` artifact lacks compile-time constants for newer
  documented foreground and battery quota outcomes.

## Independent Review

1. Scope Compliance
- PASS: the task adds typed AICore mapping without expanding API capability
  surface or persistence scope.

2. Correctness Review
- PASS: direct and nested `GenAiException` values map to stable app-owned
  categories, while the observed nested IPC detail remains distinguishable.

3. Concurrency and Lifecycle Safety
- PASS: adapter wrapping rethrows coroutine cancellation and preserves already
  mapped failures.

4. UI and UX Contract
- PASS: inference errors now render user-facing guidance from structured
  failures instead of raw SDK strings.

5. Platform Compatibility
- PASS with an explicit beta boundary: documented foreground-blocking and
  battery-quota numeric codes are mapped even though the installed
  `1.0.0-beta1` artifact does not expose their constants.

6. Test Quality
- PASS for the mapping layer: tests cover observed IPC details, wrapped
  exceptions, disk pressure, foreground blocking, and generic processing
  grouping.

7. Documentation Consistency
- PASS: the architecture taxonomy documents mappings, recovery boundaries,
  official source, and the beta-version constant mismatch.

8. Architectural Risks
- PASS: SDK-specific inspection remains inside the data adapter. Domain and UI
  code receive app-owned failure values.

9. Final Verdict
- PASS: no S0 issue remains and requested failure classes stay distinguishable
  after crossing the domain boundary.

10. Required Fixes
- None.

11. Non-blocking Recommendations
- Persist failure categories in the experiment schema during `TASK-18`.
- Capture physical-device observations for external-state-dependent outcomes.

## Later Evidence

`TASK-28` superseded the original processing-failure wording after physical
device validation retained the SDK detail: `Couldn't generate a response. Try a
different input.` Generic processing failures remain neutral and do not claim
policy rejection or foreground interruption without evidence.
