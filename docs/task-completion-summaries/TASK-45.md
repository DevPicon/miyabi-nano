# TASK-45 Completion Summary

## Task

Handle observed AICore IPC disconnect during provisioning.

## Observation

During physical-device provisioning, the app displayed the raw platform detail:

`AICore failed with error type 1-DOWNLOAD_ERROR and error code 6-IPC_ERROR: AICore service disconnected`

This is an observed transient service failure, not evidence that the device is
permanently unsupported.

## Changed Behavior

- Added an app-owned preparation-failure model with separate category,
  user-facing message, recovery guidance, and technical detail.
- Mapped the observed `IPC_ERROR` and service-disconnected detail to a
  recoverable `SERVICE_DISCONNECTED` category.
- Passed ML Kit download-callback failures through the preparation state
  machine instead of discarding them.
- Wrapped awaited provisioning exceptions without consuming coroutine
  cancellation.
- Replaced the misleading `Capability unavailable` prefix with wait-and-retry
  guidance while retaining the raw SDK detail for experiment evidence.
- Added a focused mapper test using the exact observed message.
- Inspected wrapped exception cause chains so awaited-future wrappers do not
  hide the underlying IPC detail.

## Verification

- Ran `git diff --check`.
- Result: no whitespace errors.
- Ran `./gradlew testDebugUnitTest`.
- Result: `BUILD SUCCESSFUL`.
- Ran `./gradlew assembleDebug`.
- Result: `BUILD SUCCESSFUL`.
- Re-ran `./gradlew testDebugUnitTest assembleDebug` after the
  independent-review fix.
- Result: `BUILD SUCCESSFUL`.
- Installed the debug APK on attached `SM-F731B` and `SM-S938U` devices.
- Result: both installs succeeded.
- Reproducing the external AICore disconnect remains dependent on platform
  service state and was not forced during automated verification.

## Commit

- `7cac898 TASK-45 handle AICore provisioning IPC disconnect`
- `cbcc935 TASK-45 inspect wrapped AICore disconnect failures`

## Known Limitations

- `TASK-15` still owns the complete typed mapping for documented public
  `GenAiException.ErrorCode` values.
- `TASK-26` still owns offline-before-provisioning and interrupted-download UX.
- A dated real-device evidence export remains future work.

## Independent Review

1. Scope Compliance
- PASS: the task is limited to the observed provisioning IPC disconnect and
  leaves the comprehensive documented error taxonomy to `TASK-15`.

2. Correctness Review
- PASS after correction: the mapper now recognizes direct and wrapped
  `IPC_ERROR` service-disconnect details and retains the underlying technical
  evidence.

3. Concurrency and Lifecycle Safety
- PASS: provisioning preserves `CancellationException`; lifecycle cancellation
  is not converted into a false platform failure.

4. UI and UX Contract
- PASS: a transient service disconnect no longer appears as permanent
  capability unavailability. The UI gives wait-and-retry guidance and keeps a
  separate technical detail for experimentation.

5. Platform Compatibility
- PASS with bounded evidence: debug APK installation succeeded on attached
  `SM-F731B` and `SM-S938U` devices. The external AICore disconnect was observed
  manually and was not forced during automated verification.

6. Test Quality
- PASS for the scoped change: tests cover the exact observed detail, a wrapped
  exception, retained technical evidence, retry guidance, and absence of an
  unsupported-device claim.

7. Documentation Consistency
- PASS: the task summary distinguishes observation, implemented behavior, and
  future comprehensive mapping work.

8. Architectural Risks
- PASS: SDK-specific parsing remains in the ML Kit adapter layer. The domain
  receives an app-owned failure model.

9. Final Verdict
- PASS: no S0 issue remains. One wrapped-exception robustness gap found during
  review was fixed in `cbcc935`.

10. Required Fixes
- None.

11. Non-blocking Recommendations
- Add the complete documented public error-code mapping in `TASK-15`.
- Capture dated runtime evidence when the external disconnect recurs.
