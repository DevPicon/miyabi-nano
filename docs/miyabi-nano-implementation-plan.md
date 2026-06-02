# Miyabi Nano: Android OS-Supported On-Device AI Implementation Plan

## 1. Portfolio Position

`miyabi-nano` is the Android lane for studying OS-supported on-device AI. It is not
an Android port of `iki-nano`, and parity with an iOS self-managed runtime is not
the goal.

The core question is:

> What changes when the platform vendor supplies the model execution path through
> Android AICore and ML Kit GenAI APIs, while the application still owns
> capability detection, provisioning UX, lifecycle behavior, failure handling,
> measurement, and product boundaries?

The repository should become an evidence-backed engineering case study, not a
feature catalog and not a wrapper demo.

## 2. Executive Assessment

### Current scores

| Dimension | Score | Assessment |
| --- | ---: | --- |
| Strategic alignment | 5/10 | The repository uses the correct Android-native path, but its narrative still reads like a summarization demo rather than an OS-supported AI engineering study. |
| Technical credibility | 2/10 | There is real ML Kit integration and an initial measurement scaffold, but the current branch does not compile and the repository lacks real-device evidence, lifecycle rigor, benchmark methodology, failure taxonomy, and meaningful tests. |

### Bottom line

The project has a credible seed: it integrates ML Kit GenAI summarization,
proofreading, and rewriting clients backed by Gemini Nano, checks summarizer
availability on launch, exposes model-download progress, persists inference
records with Room, and includes reusable test inputs.

It is not yet conference-ready. The current visible experience overstates what
has been proven. The app displays five capability entry points, but the main
download/status surface checks only the summarizer. The generic inference path
calls clients directly without capability-specific preparation. The README makes
strong offline and privacy claims without a reproducible validation protocol.
The metrics card presents rough timings and process-heap deltas as performance
evidence without benchmark controls.

Baseline verification also fails at `:app:compileDebugKotlin`. The generic
inference path uses incompatible await/result handling for the ML Kit client
return types, and `InferenceViewModel` is missing the `TestCase` model import.
Restoring a compiling baseline is the first implementation prerequisite.

The highest-value work is not adding more GenAI APIs. It is making the platform
constraints observable, testable, and teachable.

## 3. Repository Evidence

### What is technically serious

- `GenAiModule` creates real ML Kit GenAI clients for summarization,
  proofreading, and three rewriting configurations. This is the correct
  high-level Android OS-supported lane.
- `ModelDownloadViewModel` maps `FeatureStatus` values and exposes provisioning
  progress for the summarizer.
- `InferenceUseCase` routes five capability variants and emits initial latency,
  token-estimate, character-count, and process-memory observations.
- Room persistence provides a foundation for experiment history instead of
  screenshot-only demonstrations.
- `TestDataRepository` supplies reusable input fixtures, which is the beginning
  of repeatable experiments.

### What is superficial or misleading

- The current `main` branch does not compile. Multi-capability work was committed
  without a successful baseline build.
- The main screen presents five capabilities as equally ready, while model
  status and download lifecycle are checked only through the summarizer.
- The active generic inference path does not call `checkFeatureStatus()` before
  inference and does not explain adapter downloads, transient provisioning, or
  unsupported configurations.
- The old summarization flow and the new generic flow coexist. The old flow is
  still wired in navigation but is no longer reachable from the visible main
  menu. Keeping both flows makes lifecycle ownership and closure behavior harder
  to reason about.
- `InferenceUseCase` measures elapsed wall-clock time around one request and
  labels it inference latency. It does not distinguish cold preparation, warm
  inference, queueing, download wait, or UI-perceived time.
- `MemoryTracker.getPeakMemoryMB()` returns the runtime maximum heap limit, not
  observed peak memory. The metrics UI therefore risks teaching an incorrect
  interpretation.
- Token counts are heuristic estimates. They may be useful for grouping inputs,
  but they are not model tokenizer results and must be labeled accordingly.
- Raw input and generated output are persisted to Room. That weakens the privacy
  story unless the app clearly treats storage as an opt-in experiment mode,
  provides deletion controls, and excludes records from backup.
- Backup is enabled while backup-rule files remain template placeholders. A
  privacy-preserving demo should not leave local experiment text eligible for
  implicit cloud backup.
- The README is stale and overconfident. It describes summarization-only
  architecture, claims automatic download handling and graceful unsupported
  behavior more broadly than the active flow proves, contains a December 2024
  device list, and uses absolute phrases such as `100% offline`.
- Unit and instrumentation tests are generated placeholders. They provide no
  evidence for state transitions, lifecycle interruption, persistence, or
  unsupported-device behavior.

## 4. Narrative Support Matrix

| Narrative claim | Current support | Evidence gap |
| --- | --- | --- |
| Android with OS-supported AI | Partial | The integration uses ML Kit GenAI APIs, but the docs do not clearly explain the AICore boundary or what Android owns versus what the app owns. |
| Practical Gemini Nano / AICore experimentation | Partial | Real clients exist, but experiments are not versioned, exported, or reproducible. The app does not capture device, OS, app, API, or base-model context. |
| Device support reality | Weak | Only summarizer status is shown. No capability matrix, device fingerprint, AICore compatibility explanation, unlocked-bootloader caveat, or unsupported-device evidence exists. |
| Latency and UX implications | Weak | A single elapsed time is recorded. There is no cold/warm methodology, streaming comparison, distribution reporting, or user-perceived timing. |
| Battery and thermal implications | Missing | No battery, thermal, sustained-run, or quota experiment exists. |
| Offline behavior after provisioning | Claimed, not demonstrated | No explicit offline validation workflow or evidence artifact exists. |
| Platform constraints and failure modes | Weak | Exceptions are reduced to generic messages. There is no handling matrix for AICore incompatibility, disk pressure, quotas, background blocking, cancellation, policy rejection, or transient unavailability. |
| Offline-first UX | Weak | The UI exposes a download button, but does not model provisioning prerequisites, interrupted downloads, retry semantics, or per-capability readiness. |
| Privacy-preserving AI | Weak | Inference is on-device, but raw experiment text is persisted and backup remains enabled without exclusions. |

## 5. Platform Reality To Teach

The implementation and educational content must distinguish vendor-provided
runtime behavior from application responsibilities.

### Android / AICore responsibilities

- Execute supported Gemini Nano inference on-device.
- Share the system-managed base model across applications where available.
- Apply device- and model-version-dependent constraints.
- Enforce foreground-only inference and per-app quotas.
- Surface feature readiness and download lifecycle through ML Kit.

### Application responsibilities

- Check each configured capability at runtime.
- Explain unsupported, downloadable, downloading, and available states.
- Provide honest provisioning and offline UX.
- Handle typed failures and retries without promising guaranteed availability.
- Cancel or reconcile work when the app loses foreground eligibility.
- Measure cold and warm behavior with enough context to reproduce results.
- Minimize, disclose, and control storage of user text.
- Document realistic use-case boundaries rather than implying general-purpose
  local LLM capability.

Official references:

- [ML Kit GenAI API overview](https://developers.google.com/ml-kit/genai)
- [Gemini Nano in Android AICore](https://developer.android.com/ai/aicore)
- [Summarization API](https://developers.google.com/ml-kit/genai/summarization/android)
- [Proofreading API](https://developers.google.com/ml-kit/genai/proofreading/android)
- [Rewriting API](https://developers.google.com/ml-kit/genai/rewriting/android)
- [GenAI error codes](https://developers.google.com/android/reference/com/google/mlkit/genai/common/GenAiException.ErrorCode)

## 6. Highest-Leverage Missing Demonstrations

Priority order matters. These demonstrations should be implemented before adding
new capabilities.

| Priority | Demonstration | Why it matters |
| ---: | --- | --- |
| 1 | Capability-specific availability and provisioning matrix | The central engineering problem is device reality, not API invocation. |
| 2 | Unsupported-device and not-yet-ready UX | A conference demo must show the honest failure path, not only the happy path on one flagship phone. |
| 3 | Cold, warm, and repeated-run latency methodology | A single timer is not benchmark evidence. |
| 4 | Offline-after-provisioning experiment | Offline-first is a core claim and needs a reproducible proof protocol. |
| 5 | Foreground loss, cancellation, rotation, and process-recreation behavior | AICore inference is foreground-only; lifecycle handling is a first-class platform constraint. |
| 6 | Typed failure-mode explorer | The platform exposes meaningful error codes that should become teachable UX states. |
| 7 | Sustained-run battery, thermal, and quota experiment | Android explicitly exposes a battery-use quota failure. This is a stronger story than speculative power claims. |
| 8 | Experiment export with device and model metadata | Conference material requires evidence that can be compared and reviewed. |
| 9 | Privacy-mode storage policy | Local inference is not enough if raw text is silently persisted or backed up. |
| 10 | Realistic use-case boundary document | The strongest conclusion may be where OS-supported AI should not be used. |

## 7. Implementation Plan

### Task numbering and execution order

Task IDs are stable references for commits, reviews, and completion summaries.
Tasks must be executed in the listed phase order unless a documented dependency
requires a different sequence. A task is not complete when code is written. It
is complete only after satisfying the closure rules in `AGENT.md`.

Use `TASK-XXA`, `TASK-XXB`, `TASK-XXC`, and so on only for remediation subtasks
created when an independent Android review reports an `S0` issue against parent
task `TASK-XX`.

| Type | Phase | Purpose |
| --- | --- | --- |
| Baseline | Phase 0A | Restore a verifiable baseline |
| Documentation | Phase 0B | Correct the narrative and freeze feature growth |
| Lifecycle | Phase 1 | Unify capability lifecycle and unsupported-device UX |
| Experimentation | Phase 2 | Build a credible experiment harness |
| Offline and interruption | Phase 3 | Prove offline-first and lifecycle behavior |
| Sustained use | Phase 4 | Measure sustained-use boundaries |
| Content | Phase 5 | Package privacy, education, and conference evidence |

### Task status register

Update this register when a task starts, becomes blocked, or completes. Add S0
remediation subtasks directly after their parent using suffixes such as
`TASK-12A`.

| Task | Status | Task | Status | Task | Status | Task | Status |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `TASK-01` | `NOT STARTED` | `TASK-12` | `NOT STARTED` | `TASK-23` | `NOT STARTED` | `TASK-34` | `NOT STARTED` |
| `TASK-02` | `NOT STARTED` | `TASK-13` | `NOT STARTED` | `TASK-24` | `NOT STARTED` | `TASK-35` | `NOT STARTED` |
| `TASK-03` | `NOT STARTED` | `TASK-14` | `NOT STARTED` | `TASK-25` | `NOT STARTED` | `TASK-36` | `NOT STARTED` |
| `TASK-04` | `NOT STARTED` | `TASK-15` | `NOT STARTED` | `TASK-26` | `NOT STARTED` | `TASK-37` | `NOT STARTED` |
| `TASK-05` | `NOT STARTED` | `TASK-16` | `NOT STARTED` | `TASK-27` | `NOT STARTED` | `TASK-38` | `NOT STARTED` |
| `TASK-06` | `NOT STARTED` | `TASK-17` | `NOT STARTED` | `TASK-28` | `NOT STARTED` | `TASK-39` | `NOT STARTED` |
| `TASK-07` | `NOT STARTED` | `TASK-18` | `NOT STARTED` | `TASK-29` | `NOT STARTED` | `TASK-40` | `NOT STARTED` |
| `TASK-08` | `NOT STARTED` | `TASK-19` | `NOT STARTED` | `TASK-30` | `NOT STARTED` | `TASK-41` | `NOT STARTED` |
| `TASK-09` | `NOT STARTED` | `TASK-20` | `NOT STARTED` | `TASK-31` | `NOT STARTED` | `TASK-42` | `NOT STARTED` |
| `TASK-10` | `NOT STARTED` | `TASK-21` | `NOT STARTED` | `TASK-32` | `NOT STARTED` | `TASK-43` | `NOT STARTED` |
| `TASK-11` | `NOT STARTED` | `TASK-22` | `NOT STARTED` | `TASK-33` | `NOT STARTED` | `TASK-44` | `NOT STARTED` |

### Phase 0A: Restore a verifiable baseline

**Objective:** Make the current repository buildable before architectural work
continues.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-01` | Baseline | Repair generic inference compilation | Correct ML Kit client return-type handling in `InferenceUseCase`. Confirm the result extraction matches each client API rather than forcing a shared but invalid access pattern. | None | Generic inference code compiles for summarization, proofreading, and rewriting clients. |
| `TASK-02` | Baseline | Repair inference ViewModel compilation | Add the missing `TestCase` model import and resolve any directly resulting compilation errors without expanding behavior. | None | `InferenceViewModel` compiles with the existing test-case selection flow intact. |
| `TASK-03` | Baseline | Replace placeholder test evidence | Remove at least one generated placeholder test and add a meaningful deterministic test for existing repository logic, such as token estimation, download-state progress, or fixture selection. | `TASK-01`, `TASK-02` | The test proves repository behavior and fails if that behavior regresses. |
| `TASK-04` | Baseline | Verify baseline build health | Run the unit-test and debug-build tasks after repairs. Record the original failures and final results. | `TASK-01`, `TASK-02`, `TASK-03` | `./gradlew testDebugUnitTest` and a debug build complete successfully. |

**Phase acceptance:** The repository has a compiling baseline, a meaningful
automated test, and a completion summary that records the original failures and
the exact verification tasks run after the repair.

**Risks**

- A compile fix alone can conceal deeper lifecycle problems. Do not expand the
  feature surface while restoring baseline health.

### Phase 0B: Correct the narrative and freeze feature growth

**Objective:** Establish an accurate scope before implementation expands.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-05` | Documentation | Rewrite portfolio positioning | Rewrite the README introduction around the Android AICore-backed lane and explicitly state that `miyabi-nano` is not intended to mirror an iOS self-managed runtime. | `TASK-04` | A reader can explain the vendor-managed versus app-managed boundary and why feature parity is not the goal. |
| `TASK-06` | Documentation | Replace stale device-support claims | Remove the stale December 2024 device list. Link to the official support matrix and add a dated table only for devices actually tested in this repository. | `TASK-04` | Device claims distinguish official documentation, repository observations, and untested states. |
| `TASK-07` | Documentation | Correct metrics language | Relabel current latency, heap, and token values as exploratory observations. State the limitations of each existing measurement. | `TASK-04` | README and UI-adjacent documentation do not describe the current measurements as benchmarks. |
| `TASK-08` | Documentation | Publish known-debt register | Document duplicate inference flows, summarizer-only readiness, raw-text persistence, backup exposure, placeholder tests, and missing real-device evidence. | `TASK-04` | Each known gap is explicit and points to the planned task that addresses it. |
| `TASK-09` | Documentation | Add platform glossary | Define base model, feature-specific adapter, provisioning, capability status, cold run, warm run, and offline-after-provisioning. | `TASK-05` | Terms are used consistently across README and planning documents. |

**Phase acceptance:** README claims are traceable to code, an official
reference, or a dated real-device observation. No absolute offline, privacy,
device-support, or performance claims remain without a stated boundary.

**Risks**

- Documentation can become a substitute for implementation. Phase 0B must remain
  short and must lead directly to lifecycle work.

### Phase 1: Unify capability lifecycle and unsupported-device UX

**Objective:** Make platform readiness observable for every exposed capability.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-10` | Lifecycle | Define capability preparation contract | Introduce an application-owned contract for checking readiness, starting provisioning, reporting progress, running inference, and releasing resources for each configured ML Kit capability. | `TASK-09` | Summarization, proofreading, and rewriting configurations can be addressed through one explicit contract without hiding capability differences. |
| `TASK-11` | Lifecycle | Implement per-capability readiness state machine | Model `UNAVAILABLE`, `DOWNLOADABLE`, `DOWNLOADING`, `AVAILABLE`, transient failure, and retry state for each capability. | `TASK-10` | Each state has defined entry conditions, UI meaning, and allowed user actions. |
| `TASK-12` | Lifecycle | Replace summarizer-only menu status | Replace the single summarizer download card with a capability readiness view that exposes each visible capability's actual state. | `TASK-11` | No visible capability inherits readiness from summarization. |
| `TASK-13` | Lifecycle | Unify inference orchestration | Consolidate the old summarization flow and generic flow into one lifecycle-aware orchestration path. Remove unreachable or obsolete routing after behavior is preserved. | `TASK-10`, `TASK-11` | There is one production inference path and one owner for preparation decisions. |
| `TASK-14` | Lifecycle | Gate inference by readiness | Prevent unsupported or not-yet-ready capabilities from entering an ambiguous processing state. Present a reason and recovery action where one exists. | `TASK-12`, `TASK-13` | Unsupported and downloadable states produce stable, testable UX instead of generic failures. |
| `TASK-15` | Lifecycle | Map typed AICore failures | Translate relevant `GenAiException` codes into user-facing recovery categories and structured experiment-log categories. Include incompatible AICore, unavailable feature, disk pressure, busy service, cancellation, foreground blocking, battery quota, input-size, and policy failures. | `TASK-13` | Known error codes remain distinguishable after crossing the domain boundary. |
| `TASK-16` | Lifecycle | Define ML Kit resource ownership | Choose and implement an explicit lifecycle boundary for closing and recreating summarizer, proofreader, and rewriter clients. | `TASK-10`, `TASK-13` | Resource release is deterministic and documented; closed clients are not accidentally reused. |
| `TASK-17` | Lifecycle | Test capability lifecycle | Add deterministic tests for readiness transitions, download callbacks, retry decisions, gating, failure mapping, and resource-ownership behavior. | `TASK-11`, `TASK-14`, `TASK-15`, `TASK-16` | Automated tests cover the capability lifecycle state machine and typed failures. |

**Phase acceptance:** Each visible capability reports its own readiness,
unsupported configurations have a stable UX, downloadable and downloading states
are recoverable, one inference orchestration path remains, and automated tests
cover the lifecycle state machine.

**Risks**

- Singleton ML Kit clients simplify injection but complicate close/recreate
  behavior. Define ownership before refactoring.
- Capability adapters may share a base model while still provisioning
  independently. The UI must not oversimplify this distinction.

### Phase 2: Build a credible experiment harness

**Objective:** Turn exploratory metrics into reproducible engineering evidence.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-18` | Experimentation | Define versioned experiment schema | Define a schema containing app version, device manufacturer and model, Android build, API level, capability configuration, Gemini Nano base-model name where exposed, feature status before run, connectivity, power, thermal status, run sequence, cold/warm classification, fixture ID, heuristic input size, outcome category, and timing milestones. | `TASK-17` | Schema fields are documented, versioned, and mapped to persistence. |
| `TASK-19` | Experimentation | Capture reproducibility context | Populate experiment records with device, model, configuration, and runtime context at execution time. | `TASK-18` | Two exports from different device contexts can be distinguished without external notes. |
| `TASK-20` | Experimentation | Separate timing milestones | Capture preparation wait, download time, time to first visible output where applicable, inference completion, persistence time, and total user-perceived time. | `TASK-18`, `TASK-13` | Cold, warm, provisioning, and UI-perceived durations cannot be accidentally collapsed into one latency value. |
| `TASK-21` | Experimentation | Correct process-memory reporting | Rename existing heap observations accurately or remove them from the UI until a defensible measurement exists. Do not present runtime maximum heap as observed peak memory. | `TASK-07` | Metrics UI contains no misleading peak-memory claim. |
| `TASK-22` | Experimentation | Add structured export | Export versioned experiment data as JSON and CSV with stable field names. | `TASK-18`, `TASK-19`, `TASK-20` | Another engineer can inspect and analyze raw records without querying the app database. |
| `TASK-23` | Experimentation | Define benchmark protocols | Document controlled protocols for cold start, warm inference, repeated runs, input-size bands, and capability comparisons. | `TASK-20`, `TASK-22` | Each reported benchmark category has setup steps, sample-count rules, and exclusion rules. |
| `TASK-24` | Experimentation | Strengthen controlled fixtures | Assign stable fixture IDs, size bands, capability relevance, and qualitative checks to repository test inputs. | `TASK-18` | Exported runs reference controlled fixtures rather than relying only on arbitrary pasted text. |

**Phase acceptance:** Another engineer can reproduce an experiment from the
protocol and exported record. Observations remain distinct from benchmark
summaries, and cold and warm runs cannot be combined accidentally.

**Risks**

- App-level measurements cannot prove system-level energy or memory behavior.
  Label them accurately and supplement them with Android tooling in Phase 4.

### Phase 3: Prove offline-first and lifecycle behavior

**Objective:** Demonstrate the user experience under real mobile interruptions.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-25` | Offline and interruption | Define offline-after-provisioning protocol | Document the real-device sequence: provision online, record readiness, disable connectivity, relaunch, run each prepared capability, and export evidence. | `TASK-22`, `TASK-23` | The protocol is reproducible and distinguishes offline-before-provisioning from offline-after-provisioning. |
| `TASK-26` | Offline and interruption | Implement offline provisioning UX | Add explicit UI states for offline-before-provisioning and interrupted provisioning with honest retry guidance. | `TASK-14`, `TASK-25` | Users are not told offline inference is ready when required capability assets are missing. |
| `TASK-27` | Offline and interruption | Handle configuration changes | Verify and fix rotation behavior during provisioning and inference. Preserve or reconcile state without duplicate work. | `TASK-13`, `TASK-16` | Rotation does not create duplicate inference, lose terminal state, or leave a stuck UI. |
| `TASK-28` | Offline and interruption | Handle foreground loss | Treat `BACKGROUND_USE_BLOCKED` as an expected platform outcome. Reconcile UI state when inference loses foreground eligibility. | `TASK-15` | Backgrounding during inference produces a defined, recoverable result. |
| `TASK-29` | Offline and interruption | Handle user interruption | Verify and fix rapid repeated requests, cancellation, navigation away, app relaunch, and process recreation. | `TASK-27`, `TASK-28` | User interruption never leaves duplicate work or an indefinite processing state. |
| `TASK-30` | Offline and interruption | Add lifecycle automation and device evidence | Add deterministic instrumented tests for state restoration and cancellation. Record real-device-only scenarios separately with dated evidence. | `TASK-26`, `TASK-27`, `TASK-28`, `TASK-29` | Automated checks cover deterministic behavior and the real-device report covers AICore-dependent cases. |

**Phase acceptance:** Offline behavior is demonstrated with dated exported
evidence, not inferred from documentation. Navigation, background loss,
cancellation, and configuration changes never leave a stuck processing state.

**Risks**

- Some provisioning and AICore behaviors are external-state dependent and
  cannot be made deterministic in instrumentation. Keep a manual real-device
  matrix with evidence artifacts.

### Phase 4: Measure sustained-use boundaries

**Objective:** Produce defensible conclusions about latency, battery, thermal,
and quotas.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-31` | Sustained use | Define sustained-run scenarios | Specify controlled fixtures, run counts, starting battery range, charging state, connectivity, ambient notes, and cool-down rules. | `TASK-23`, `TASK-24`, `TASK-30` | A sustained-run session can be repeated with the same constraints. |
| `TASK-32` | Sustained use | Capture thermal annotations | Capture Android thermal status before, during where practical, and after sustained-run scenarios. | `TASK-31` | Thermal annotations appear in the raw evidence with timestamps and device context. |
| `TASK-33` | Sustained use | Collect external battery and performance evidence | Use Android platform tooling outside the app. Treat in-app readings as annotations rather than system-level ground truth. | `TASK-31` | Raw tooling artifacts are retained and linked from the experiment summary. |
| `TASK-34` | Sustained use | Exercise quota boundaries | Attempt controlled quota-boundary experiments and record `BUSY` and `PER_APP_BATTERY_USE_QUOTA_EXCEEDED` outcomes when observed. Record absence of an observed quota outcome honestly. | `TASK-31`, `TASK-15` | The report distinguishes observed outcomes from documented but unobserved platform behavior. |
| `TASK-35` | Sustained use | Produce statistical summaries | Report medians, percentile ranges, variance, and sample counts instead of single-run values. | `TASK-32`, `TASK-33`, `TASK-34` | Every published performance conclusion includes methodology, sample count, and raw evidence reference. |
| `TASK-36` | Sustained use | Compare streaming and non-streaming UX | For applicable APIs, compare time to first visible output, completion time, interruption behavior, and perceived responsiveness. | `TASK-20`, `TASK-31` | The UX trade-off is documented with scoped evidence rather than preference. |

**Phase acceptance:** Results include methodology, device identifiers, sample
counts, and raw artifacts. Battery and thermal conclusions are scoped to
observed devices and protocols. The repository documents when OS-supported AI
becomes a poor UX choice.

**Risks**

- Battery experiments are noisy. Do not publish precision the protocol cannot
  support.
- Quota experiments may be difficult to reproduce. Report absence of evidence
  honestly rather than manufacturing certainty.

### Phase 5: Privacy, education, and conference package

**Objective:** Convert engineering evidence into reusable, non-hyped material.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-37` | Content | Make experiment persistence privacy-aware | Make raw user-text persistence opt-in or fixture-only by default. Separate experiment metadata retention from sensitive payload retention. | `TASK-18`, `TASK-22` | Default behavior does not silently retain arbitrary user text. |
| `TASK-38` | Content | Add data controls and backup policy | Add delete and export controls. Exclude sensitive local records from cloud backup or disable backup if that is the clearer policy. | `TASK-37` | A privacy checklist proves local data can be inspected, exported intentionally, deleted, and kept out of implicit cloud backup. |
| `TASK-39` | Content | Publish device-validation matrix | Create a dated matrix that clearly separates tested devices, observed states, official support references, and untested devices. | `TASK-30`, `TASK-35` | Device claims remain scoped and auditable. |
| `TASK-40` | Content | Publish failure-mode catalog | Document trigger, observed behavior, expected UX, recovery path, automation coverage, and real-device evidence for each relevant failure. | `TASK-15`, `TASK-30`, `TASK-34` | Every handled failure maps to code, tests, official documentation, or dated device evidence. |
| `TASK-41` | Content | Create benchmark report template | Add a reusable template covering protocol, device context, sample count, exclusions, raw artifacts, statistical summary, limitations, and negative results. | `TASK-35`, `TASK-36` | Future benchmark reports cannot omit methodological context. |
| `TASK-42` | Content | Document realistic use-case boundaries | Document supported devices and languages, foreground-only inference, provisioning dependency, quotas, input-size constraints, model-version variability, beta API status, and unsuitable use cases. | `TASK-39`, `TASK-40`, `TASK-41` | The repository states where OS-supported AI should not be used. |
| `TASK-43` | Content | Publish portfolio comparison | Explain OS-supported runtime engineering versus self-managed runtime engineering without treating either path as universally superior or requiring feature parity. | `TASK-42` | The comparison focuses on responsibility boundaries and observed trade-offs. |
| `TASK-44` | Content | Create conference talk outline | Create a CFP-ready outline grounded in repository evidence, including negative results, engineering decisions, and reusable lessons. | `TASK-39`, `TASK-40`, `TASK-41`, `TASK-42`, `TASK-43` | Every proposed talk claim is traceable to code, tests, official documentation, or dated evidence. |

**Phase acceptance:** A skeptical reviewer can trace each conference claim to
code, test output, official documentation, or a dated experiment artifact.
Privacy claims cover persistence and backup, not only network transport.
Educational docs include negative results and decision boundaries.

**Risks**

- The content can drift into product marketing. Every statement should answer:
  what was observed, under which conditions, and what remains unknown?

## 8. Verification Matrix

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

## 9. Governance Rules

`AGENT.md` is the repository governance source of truth. If this section and
`AGENT.md` diverge, follow `AGENT.md` and update this summary.

Every implementation task follows this closure sequence:

1. Implement the scoped task.
2. Verify automated checks and required real-device scenarios.
3. Commit the task with its stable `TASK-XX` ID.
4. Generate `docs/task-completion-summaries/TASK-XX.md`.
5. Run the `android-task-independent-review` skill after the summary exists.
6. Record the review result and resolve required follow-up before marking the
   task complete.

If the review reports an `S0`, keep the parent task open and append remediation
subtasks to this plan using the parent ID plus alphabetical suffixes:
`TASK-XXA`, `TASK-XXB`, `TASK-XXC`, `TASK-XXD`, and so on. Each remediation
subtask must include its own definition, type, dependency, acceptance criteria,
status, commit, summary, and independent review.

Track every task as `NOT STARTED`, `IN PROGRESS`, `BLOCKED`, or `COMPLETE`.

## 10. Content Anti-Patterns To Avoid

- **Feature checklist inflation:** Adding prompt API, image description, or more
  rewrite styles before lifecycle evidence exists.
- **API-wrapper tutorial framing:** Showing only client construction and one
  successful inference call.
- **Flagship-device theater:** Demonstrating one prepared phone and implying
  Android-wide availability.
- **Absolute privacy claims:** Ignoring local history persistence, backups,
  screenshots, logs, and exports.
- **Benchmark cosplay:** Publishing one latency value, heap delta, or battery
  screenshot as a general conclusion.
- **Runtime parity framing:** Comparing Android and iOS by feature count instead
  of comparing vendor-managed and app-managed responsibilities.
- **Happy-path-only content:** Omitting provisioning delays, disk pressure,
  foreground restrictions, quotas, model variation, and transient AICore state.
- **Hype vocabulary:** Calling a beta integration production-ready, seamless, or
  universal without evidence.

## 11. Immediate Next Task

Implement `TASK-01` first, complete Phase 0A, then proceed through Phase 0B and
Phase 1 in dependency order:

> Restore a compiling, testable baseline. Then create a capability-specific
> readiness and provisioning state machine, unify inference orchestration, map
> typed AICore failures, and add unsupported-device UX with automated
> state-transition tests.

This is the highest-leverage next step because every later claim depends on
knowing whether a capability is supported, provisioned, usable in the current
lifecycle state, and failing for a reason the application can explain.
