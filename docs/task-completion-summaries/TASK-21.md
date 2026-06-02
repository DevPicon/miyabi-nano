# TASK-21 Completion Summary

## Task

Correct process-memory reporting.

## Changed Behavior

- Renamed the process-memory delta observation to `processHeapDeltaMB`.
- Renamed the JVM heap ceiling observation to `runtimeMaxHeapMB`.
- Replaced the ambiguous UI label `Memory` with `Process heap delta`.
- Preserved legacy Room column names with `@ColumnInfo` so existing experiment
  history is not erased by a destructive schema migration.
- Updated the repository evaluation to describe the corrected measurement
  boundary.

## Verification

- Ran `git diff --check`.
- Result: no whitespace errors.
- Searched source and documentation for stale peak-memory claims and symbols.
- Ran `./gradlew testDebugUnitTest assembleDebug`.
- Result: `BUILD SUCCESSFUL`.
- Re-ran `./gradlew testDebugUnitTest assembleDebug` after the
  independent-review schema compatibility fix.
- Result: `BUILD SUCCESSFUL`.

## Commit

- `1d2e936 TASK-21 correct process-memory reporting`
- `140d2d4 TASK-21 preserve metrics schema compatibility`

## Known Limitations

- Process-heap delta remains an app-level observation, not model memory, total
  process peak memory, or system memory.
- External tooling remains required for defensible system-level evidence.

## Independent Review

1. Scope Compliance
- PASS: the task corrects memory terminology without expanding metrics scope.

2. Correctness Review
- PASS after correction: Kotlin and UI names match the observed values, while
  legacy Room column names preserve stored rows.

3. Concurrency and Lifecycle Safety
- PASS: no threading or lifecycle behavior changed.

4. UI and UX Contract
- PASS: the visible label now states `Process heap delta`.

5. Platform Compatibility
- PASS: no Room migration is required and existing experiment history remains
  readable.

6. Test Quality
- PASS for the scoped rename: unit tests and packaging pass. Database migration
  automation remains future hardening work.

7. Documentation Consistency
- PASS: misleading peak-memory debt was removed and the evaluation was updated.

8. Architectural Risks
- PASS: app-level observations remain explicitly distinct from system-level
  evidence.

9. Final Verdict
- PASS: no S0 issue remains. A destructive-migration risk found during review
  was fixed in `140d2d4`.

10. Required Fixes
- None.

11. Non-blocking Recommendations
- Use Android platform tooling for system-level memory evidence.
