# TASK-28 Completion Summary

## Task

Handle foreground loss.

## Changed Behavior

- Kept documented `BACKGROUND_USE_BLOCKED` code `30` as the precise
  foreground-loss category.
- Reclassified generic request, response, and generation processing errors as
  neutral `PROCESSING_FAILED` outcomes rather than unsupported policy rejection
  or inferred foreground interruption.
- Added retry-once guidance. Different input is suggested only if the same
  input fails repeatedly, and foreground-only execution remains explicit.
- Removed the unsupported `POLICY_REJECTION` category.
- Retained raw SDK technical detail in inference UI state and displayed it in
  the error card for physical-device evidence capture.
- Recorded the reported physical-device observation and attached-device
  metadata for Samsung `SM-F731B`, Android API `36`, build
  `BP2A.250605.031.A3.F731BXXS6FZC5`.

## Verification

- Ran `git diff --check`.
- Result: no whitespace errors.
- Ran `./gradlew testDebugUnitTest assembleDebug`.
- Result: `BUILD SUCCESSFUL`.
- Physical-device revalidation remains required: background inference on the
  attached Samsung device should display the new interruption guidance and
  recover after returning to the app.

## Commit

- `edec8fd TASK-28 handle ambiguous foreground interruptions`
- `8da2fb7 TASK-28 retain inference failure details`
- `6a56570 TASK-28 keep generic processing failures neutral`

## Known Limitations

- A later same-input observation retained `Couldn't generate a response. Try a
  different input.` The app cannot claim that a generic processing code was
  emitted specifically because of backgrounding or modal opening.
- Generic processing failures remain intentionally neutral.
- Physical-device revalidation is deferred.

## Independent Review

1. Scope Compliance
- PASS: the task narrows foreground-loss handling without adding unrelated
  lifecycle behavior.

2. Correctness Review
- PASS after correction: documented code `30` remains precise, generic
  processing errors remain neutral, and raw SDK detail survives the inference
  UI boundary. Repeated same-input failure is not mislabeled as stale UI,
  foreground loss, or poisoned client state.

3. Concurrency and Lifecycle Safety
- PASS: existing structured ViewModel coroutine ownership remains unchanged.

4. UI and UX Contract
- PASS after correction: the UI suggests one retry before different input and
  displays technical detail for physical-device evidence capture.

5. Platform Compatibility
- PASS with a physical-device boundary: forward-compatible numeric code `30`
  remains documented separately from generic processing codes.

6. Test Quality
- PASS for deterministic mapping behavior. Physical-device background-loss
  revalidation remains required because AICore emission behavior is external
  to deterministic unit tests.

7. Documentation Consistency
- PASS: the taxonomy and dated observation distinguish observed behavior from
  inference.

8. Architectural Risks
- PASS: structured failures remain behind the capability boundary.

9. Final Verdict
- PARTIAL: no S0 issue remains and implementation is ready for physical-device
  revalidation. Keep `TASK-28` open until the Samsung rerun records the neutral
  guidance and compares same-input versus different-input outcomes.

10. Required Fixes
- Completed in `8da2fb7`: retain and display raw inference failure detail.
- Completed in `6a56570`: keep generic processing failures neutral.
- Pending manual verification: rerun on attached Samsung `SM-F731B`, comparing
  same input, different input, navigation away and back, and app relaunch.

11. Non-blocking Recommendations
- Feed latest failure detail into the global diagnostics modal planned in
  `TASK-51`.
