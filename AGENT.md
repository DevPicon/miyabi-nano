# Miyabi Nano Agent Governance

This file is the source of truth for repository governance. `CLAUDE.md`,
`GEMINI.md`, planning documents, and task summaries may add context, but they
must not override this file.

## 1. Repository Purpose

`miyabi-nano` is the Android lane for studying OS-supported on-device AI through
Android AICore and ML Kit GenAI APIs. It is not intended to mirror an iOS
self-managed runtime.

Optimize for:

- technical credibility
- offline-first evidence
- privacy-preserving behavior
- realistic platform constraints
- reusable educational material
- conference-worthy engineering lessons

Do not optimize for feature count, hype, or superficial API wrapping.

## 2. Planning Source

Use `docs/miyabi-nano-implementation-plan.md` as the ordered implementation
backlog.

- Use sequential task IDs: `TASK-01`, `TASK-02`, and so on.
- Keep task type, phase, dependencies, definition, and acceptance criteria as
  metadata in the plan.
- Execute tasks in dependency order.
- Track each task as `NOT STARTED`, `IN PROGRESS`, `BLOCKED`, or `COMPLETE`.
- Only mark a task `COMPLETE` after the closure workflow below finishes.

## 3. Required Task Closure Workflow

Every implementation task must follow this sequence:

1. **Implement:** Complete only the scoped task. Do not batch unrelated work.
2. **Verify:** Run relevant automated checks and required real-device scenarios.
   Record what ran, what passed, and what could not run.
3. **Commit:** Create one intentional commit for the task. Include the stable
   task ID in the commit message.
4. **Summarize:** Create a completion summary containing changed behavior,
   verification evidence, commit hash, known limitations, and unresolved risks.
5. **Review:** After the summary exists, run the
   `android-task-independent-review` skill against the completed task, commit,
   summary, and relevant diff.
6. **Resolve review outcome:** Record the independent-review verdict and
   findings in the completion summary. Keep the task open until required review
   follow-up is resolved.

The completion summary must be stored under:

`docs/task-completion-summaries/TASK-XX.md`

## 4. Independent Android Review Gate

Use:

`/Users/devpicon/.agents/skills/android-task-independent-review/SKILL.md`

The review must use the skill's severity model and required output structure.

If the review reports an `S0` issue:

1. Do not mark the parent task `COMPLETE`.
2. Add one or more remediation subtasks to
   `docs/miyabi-nano-implementation-plan.md`.
3. Use the parent ID with alphabetical suffixes in discovery order:
   `TASK-XXA`, `TASK-XXB`, `TASK-XXC`, `TASK-XXD`, and so on.
4. Define each remediation subtask with type, description, dependency,
   acceptance criteria, and status.
5. Implement, verify, commit, summarize, and independently review every
   remediation subtask using the same closure workflow.
6. Re-run the independent review for the parent task after remediation.

For `S1`, `S2`, and `S3` findings, record the finding and disposition in the
completion summary. Required fixes must be resolved before task closure or added
to the plan as explicit follow-up tasks.

## 5. Evidence Rules

- Do not treat emulator-only behavior as proof for AICore- or device-dependent
  claims.
- Do not publish performance numbers without protocol, device context, sample
  count, and raw evidence.
- Do not use `100% offline`, `private`, `supported`, or `benchmark` as shorthand.
  State the verified boundary.
- Treat negative results as valuable evidence.
- Do not add a new capability while an exposed capability bypasses readiness,
  provisioning, failure, or lifecycle handling.

## 6. Content Rules

Avoid:

- feature checklist inflation
- happy-path-only demos
- flagship-device theater
- absolute privacy claims
- benchmark cosplay
- runtime parity framing
- production-readiness claims for beta APIs without evidence

Every technical statement should answer:

- What was observed?
- Under which conditions?
- What remains unknown?
