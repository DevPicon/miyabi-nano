# Miyabi Nano Implementation Plan

## Related Documents

- [Portfolio positioning](portfolio-positioning.md)
- [Repository evaluation](repository-evaluation.md)
- [Platform responsibility model](platform-responsibility-model.md)
- [Evidence priorities](evidence-priorities.md)
- [Verification strategy](verification-strategy.md)
- [Technical content strategy](content-strategy.md)
- [Known debt register](known-debt-register.md)
- [Platform glossary](platform-glossary.md)
- [Agent governance](../AGENT.md)

## Task Numbering And Execution Order

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
| Capability expansion | Phase 1B | Add API families only after lifecycle credibility |
| Experimentation | Phase 2 | Build a credible experiment harness |
| Offline and interruption | Phase 3 | Prove offline-first and lifecycle behavior |
| Sustained use | Phase 4 | Measure sustained-use boundaries |
| Content | Phase 5 | Package privacy, education, and conference evidence |

## Task Status Register

Update this register when a task starts, becomes blocked, or completes. Add S0
remediation subtasks directly after their parent using suffixes such as
`TASK-12A`.

| Task | Status | Task | Status | Task | Status | Task | Status |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `TASK-01` | `COMPLETE` | `TASK-12` | `COMPLETE` | `TASK-23` | `NOT STARTED` | `TASK-34` | `NOT STARTED` |
| `TASK-02` | `COMPLETE` | `TASK-13` | `COMPLETE` | `TASK-24` | `COMPLETE` | `TASK-35` | `NOT STARTED` |
| `TASK-03` | `COMPLETE` | `TASK-14` | `COMPLETE` | `TASK-25` | `NOT STARTED` | `TASK-36` | `NOT STARTED` |
| `TASK-04` | `COMPLETE` | `TASK-15` | `COMPLETE` | `TASK-26` | `NOT STARTED` | `TASK-37` | `NOT STARTED` |
| `TASK-05` | `COMPLETE` | `TASK-16` | `COMPLETE` | `TASK-27` | `NOT STARTED` | `TASK-38` | `NOT STARTED` |
| `TASK-06` | `COMPLETE` | `TASK-17` | `COMPLETE` | `TASK-28` | `IN PROGRESS` | `TASK-39` | `NOT STARTED` |
| `TASK-07` | `COMPLETE` | `TASK-18` | `COMPLETE` | `TASK-29` | `NOT STARTED` | `TASK-40` | `NOT STARTED` |
| `TASK-08` | `COMPLETE` | `TASK-19` | `COMPLETE` | `TASK-30` | `NOT STARTED` | `TASK-41` | `NOT STARTED` |
| `TASK-09` | `COMPLETE` | `TASK-20` | `COMPLETE` | `TASK-31` | `NOT STARTED` | `TASK-42` | `NOT STARTED` |
| `TASK-10` | `COMPLETE` | `TASK-21` | `COMPLETE` | `TASK-32` | `NOT STARTED` | `TASK-43` | `NOT STARTED` |
| `TASK-11` | `COMPLETE` | `TASK-22` | `NOT STARTED` | `TASK-33` | `NOT STARTED` | `TASK-44` | `NOT STARTED` |
| `TASK-45` | `COMPLETE` | `TASK-46` | `COMPLETE` | `TASK-47` | `COMPLETE` | `TASK-48` | `NOT STARTED` |
| `TASK-49` | `COMPLETE` | `TASK-50` | `NOT STARTED` |  |  |  |  |
| `TASK-51` | `IN PROGRESS` | `TASK-52` | `IN PROGRESS` | `TASK-53` | `NOT STARTED` | `TASK-54` | `IN PROGRESS` |
| `TASK-55` | `IN PROGRESS` |  |  |  |  |  |  |
| `TASK-56` | `NOT STARTED` |  |  |  |  |  |  |
| `TASK-57` | `IN PROGRESS` |  |  |  |  |  |  |

## Phase 0A: Restore A Verifiable Baseline

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

**Risk:** A compile fix alone can conceal deeper lifecycle problems. Do not
expand the feature surface while restoring baseline health.

## Phase 0B: Correct The Narrative And Freeze Feature Growth

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

**Risk:** Documentation can become a substitute for implementation. Phase 0B
must remain short and lead directly to lifecycle work.

## Phase 1: Unify Capability Lifecycle And Unsupported-Device UX

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
| `TASK-45` | Lifecycle | Handle observed AICore IPC disconnect during provisioning | Map an observed `1-DOWNLOAD_ERROR / 6-IPC_ERROR` service disconnect to a recoverable preparation failure. Show wait-and-retry guidance without labeling the device unsupported, retain the raw SDK detail for evidence, and keep the broader public error-code catalog in `TASK-15`. | `TASK-12` | Provisioning IPC disconnects produce a stable retry UX with a user-facing explanation and separate technical detail. |
| `TASK-46` | Lifecycle | Retrieve Gemini Nano base-model identity | Call `getBaseModelName()` through the app-owned capability boundary after a suitable capability becomes ready. Model loading, unavailable identity, transient failure, and retrieved identity explicitly without assuming one name applies before AICore can expose it. | `TASK-10`, `TASK-45` | The app can display the retrieved Gemini Nano base-model name when available and can distinguish unknown identity from unsupported device state. |
| `TASK-47` | Lifecycle | Simplify home bootstrap and support UX | Replace the per-capability download panel on the home screen with a concise device-support and Gemini Nano identity summary plus one initial setup action where required. Keep the experiment list as the primary navigation. Move capability-specific readiness checks, provisioning prompts, and fixture/input availability into each experiment flow. | `TASK-13`, `TASK-14`, `TASK-46` | The home screen explains whether on-device AI is ready without implying that one bootstrap download proves every capability asset is installed. Each experiment gates text entry, baked fixtures, and inference using its configured capability status. |

**Phase acceptance:** Each visible capability reports its own readiness,
unsupported configurations have a stable UX, downloadable and downloading states
are recoverable, one inference orchestration path remains, and automated tests
cover the lifecycle state machine.

**Risks:** Singleton ML Kit clients simplify injection but complicate
close/recreate behavior. Capability adapters may share a base model while still
provisioning independently.

## Phase 1B: Add Capability Families Without Hiding Support Differences

**Objective:** Expand the educational surface only after the shared lifecycle
path is proven. Preserve separate API maturity levels, support matrices,
provisioning behavior, inputs, and failure modes.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-48` | Capability expansion | Add image-description experiment | Add the ML Kit GenAI Image Description Beta API as an image-input experiment. Reuse the app-owned readiness, provisioning, failure, model-identity, and resource-ownership boundaries without pretending that text-capability support proves image-description support. | `TASK-17`, `TASK-47` | Image description has capability-specific readiness UX, controlled image fixtures, and clearly labeled Beta status. |
| `TASK-49` | Capability expansion | Investigate speech-recognition scope | Verify whether a current official Android on-device speech-recognition API belongs in this repository and document its platform boundary before adding code. Do not describe Speech Recognition as an ML Kit GenAI API unless an official primary source supports that claim. | `TASK-17`, `TASK-47` | The plan either cites a primary Android API source and defines a separately scoped experiment, or records why speech recognition remains out of scope. |
| `TASK-50` | Capability expansion | Add Prompt API experiment | Add the Prompt Alpha API as a separately gated experiment. Expose the retrieved Nano model version, model-family support boundary, prompt fixture identity, and safety limitations. Do not route arbitrary prompts through feature-specific API assumptions. | `TASK-17`, `TASK-46`, `TASK-47` | Prompt experiments run only on a verified supported configuration and record whether the device exposes a compatible Nano model family. |

## Phase 1C: Make Platform Context Inspectable

**Objective:** Keep the primary experiment UI focused while making secondary
platform evidence available throughout the app.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-51` | Lifecycle | Add global diagnostics modal | Add an app-level secondary-information action available from the home screen and experiment screens. Display stable app, database, experiment-schema, device, Android-build, API-level, Gemini Nano identity, and readiness information. Include latest successful-run context where available without presenting current annotations as benchmark evidence. | `TASK-19`, `TASK-46`, `TASK-47` | A reviewer can inspect the Room database version and reproducibility context without querying Room directly. The modal distinguishes app-level diagnostics from latest-run observations. |
| `TASK-52` | Lifecycle | Make primary screens system-bar safe | Use Material scaffold and inset-aware top bars for the home and experiment surfaces. Place the Gemini Nano model card above the experiment list because model readiness is the primary platform signal. | `TASK-47` | Home and experiment headers do not overlap system bars, and the Nano model summary appears before experiment navigation. |
| `TASK-53` | Lifecycle | Upgrade Compose to remove frame-rate log spam | Upgrade the Compose BOM and resolved UI artifacts to a version that includes the AndroidX fix for continuous `setRequestedFrameRate frameRate=NaN` logcat spam on API 35+ devices. Verify the repository no longer emits the repeated warning during normal text-entry, scrolling, and redraw paths, and record the exact upgraded versions and verification date. | `TASK-52` | The app resolves to a Compose UI version that includes the AndroidX fix, normal interaction on a current Android 15 or Android 16 device no longer floods logcat with repeated `setRequestedFrameRate frameRate=NaN` messages, and the dependency change is documented with verification evidence. |
| `TASK-54` | Lifecycle | Enforce summarization article-input boundaries | Encode the official ML Kit article-summarization minimum of more than 400 characters, surface the documented 300-word best-performing guidance without treating it as a hard requirement, and strengthen controlled summarization fixtures. Record that `AVAILABLE` status proves configured-feature readiness but not successful generation for every input. | `TASK-24`, `TASK-28` | Invalid short articles cannot be submitted, valid but suboptimal articles are labeled honestly, at least one baked summarization article is in the recommended range, and tests enforce the boundary. |
| `TASK-55` | Lifecycle | Expose last SDK request snapshot | Add a diagnostics-only, in-memory snapshot of the latest attempted feature-API request. For summarization, display the exact article text and public client options while stating that ML Kit does not expose its platform-managed internal prompt. Do not add implicit payload persistence. | `TASK-51`, `TASK-54` | After a successful or failed attempt, diagnostics show the exact app-submitted SDK request fields and input text without claiming access to an internal prompt. |
| `TASK-56` | Lifecycle | Investigate summarization recovery boundary | Reproduce the summarization-specific repeated generation failure and distinguish which recovery boundary matters: new request, different article, experiment navigation, app process restart, app-data clear, or uninstall and reinstall. Record configured-feature readiness, Nano identity, request snapshot, and SDK detail before and after recovery. Do not add an unsupported model-reset action: ML Kit exposes client close and recreation but no public feature-reset API. | `TASK-28`, `TASK-54`, `TASK-55` | A dated physical-device report states the smallest observed recovery action and separates app-client recreation, process restart, app-data reset, and AICore-managed model state. Any recovery UX is based on observed evidence rather than guesswork. |
| `TASK-57` | Documentation | Publish newcomer concept guides | Add a documentation entrypoint, explain the AICore responsibility boundary, document benefits and trade-offs of OS-supported model execution, and publish Mermaid diagrams for provisioning, inference, and recovery analysis. Link the guides from the README and remove stale summarization-only onboarding claims. | `TASK-09`, `TASK-47`, `TASK-55` | A new reader can explain what AICore owns, what the app still owns, why OS support is valuable, and why runtime readiness does not eliminate lifecycle evidence work. |

**Phase acceptance:** New API families reuse lifecycle evidence where valid and
remain separately gated where platform support differs. The repository explains
why a working text feature-specific API does not prove Prompt API, Advanced
Speech Recognition, or Image Description availability.

**Risk:** Adding visible capabilities can create feature-checklist inflation.
Do not start Phase 1B until `TASK-17` closes the shared lifecycle with tests.

## Phase 2: Build A Credible Experiment Harness

**Objective:** Turn exploratory metrics into reproducible engineering evidence.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-18` | Experimentation | Define versioned experiment schema | Define a schema containing app version, device manufacturer and model, Android build, API level, capability configuration, Gemini Nano base-model name where exposed, feature status before run, connectivity, power, thermal status, run sequence, cold/warm classification, fixture ID, heuristic input size, outcome category, and timing milestones. | `TASK-17`, `TASK-46` | Schema fields are documented, versioned, and mapped to persistence. |
| `TASK-19` | Experimentation | Capture reproducibility context | Populate experiment records with device, model, configuration, and runtime context at execution time. | `TASK-18` | Two exports from different device contexts can be distinguished without external notes. |
| `TASK-20` | Experimentation | Separate timing milestones | Capture preparation wait, download time, time to first visible output where applicable, inference completion, persistence time, and total user-perceived time. | `TASK-18`, `TASK-13` | Cold, warm, provisioning, and UI-perceived durations cannot be accidentally collapsed into one latency value. |
| `TASK-21` | Experimentation | Correct process-memory reporting | Rename existing heap observations accurately or remove them from the UI until a defensible measurement exists. Do not present runtime maximum heap as observed peak memory. | `TASK-07` | Metrics UI contains no misleading peak-memory claim. |
| `TASK-22` | Experimentation | Add structured export | Export versioned experiment data as JSON and CSV with stable field names. | `TASK-18`, `TASK-19`, `TASK-20` | Another engineer can inspect and analyze raw records without querying the app database. |
| `TASK-23` | Experimentation | Define benchmark protocols | Document controlled protocols for cold start, warm inference, repeated runs, input-size bands, and capability comparisons. | `TASK-20`, `TASK-22` | Each reported benchmark category has setup steps, sample-count rules, and exclusion rules. |
| `TASK-24` | Experimentation | Strengthen controlled fixtures | Assign stable fixture IDs, size bands, capability relevance, and qualitative checks to repository test inputs. | `TASK-18` | Exported runs reference controlled fixtures rather than relying only on arbitrary pasted text. |

**Phase acceptance:** Another engineer can reproduce an experiment from the
protocol and exported record. Observations remain distinct from benchmark
summaries, and cold and warm runs cannot be combined accidentally.

**Risk:** App-level measurements cannot prove system-level energy or memory
behavior. Label them accurately and supplement them with Android tooling.

## Phase 3: Prove Offline-First And Lifecycle Behavior

**Objective:** Demonstrate the user experience under real mobile interruptions.

| ID | Type | Task | Definition | Dependency | Task-level acceptance |
| --- | --- | --- | --- | --- | --- |
| `TASK-25` | Offline and interruption | Define offline-after-provisioning protocol | Document the real-device sequence: provision online, record readiness, disable connectivity, relaunch, run each prepared capability, and export evidence. | `TASK-22`, `TASK-23` | The protocol is reproducible and distinguishes offline-before-provisioning from offline-after-provisioning. |
| `TASK-26` | Offline and interruption | Implement offline provisioning UX | Add explicit UI states for offline-before-provisioning and interrupted provisioning with honest retry guidance. | `TASK-14`, `TASK-25` | Users are not told offline inference is ready when required capability assets are missing. |
| `TASK-27` | Offline and interruption | Handle configuration changes | Verify and fix rotation behavior during provisioning and inference. Preserve or reconcile state without duplicate work. | `TASK-13`, `TASK-16` | Rotation does not create duplicate inference, lose terminal state, or leave a stuck UI. |
| `TASK-28` | Offline and interruption | Handle foreground loss | Treat `BACKGROUND_USE_BLOCKED` as an expected platform outcome. Reconcile UI state when inference loses foreground eligibility. Preserve raw SDK detail when a less-specific processing error occurs, without claiming foreground loss or blaming valid input without evidence. | `TASK-15` | Backgrounding during inference produces a defined, recoverable result when the SDK exposes it. Generic processing failures remain honestly labeled and retain technical detail. |
| `TASK-29` | Offline and interruption | Handle user interruption | Verify and fix rapid repeated requests, cancellation, navigation away, in-app diagnostics modal opening during active inference, app relaunch, and process recreation. Distinguish a repeated same-input AICore response-generation failure from a poisoned reusable client or stuck UI state. | `TASK-27`, `TASK-28` | User interruption never leaves duplicate work or an indefinite processing state. Repeated failures can be distinguished from stale UI and client-reuse defects. |
| `TASK-30` | Offline and interruption | Add lifecycle automation and device evidence | Add deterministic instrumented tests for state restoration and cancellation. Record real-device-only scenarios separately with dated evidence. | `TASK-26`, `TASK-27`, `TASK-28`, `TASK-29` | Automated checks cover deterministic behavior and the real-device report covers AICore-dependent cases. |

**Phase acceptance:** Offline behavior is demonstrated with dated exported
evidence, not inferred from documentation. Navigation, background loss,
cancellation, and configuration changes never leave a stuck processing state.

**Risk:** Some provisioning and AICore behaviors are external-state dependent
and cannot be deterministic in instrumentation. Keep a manual real-device
matrix with evidence artifacts.

## Phase 4: Measure Sustained-Use Boundaries

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

**Risks:** Battery experiments are noisy. Quota experiments may be difficult to
reproduce. Report uncertainty honestly.

## Phase 5: Privacy, Education, And Conference Package

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

**Risk:** Content can drift into product marketing. Every statement should state
what was observed, under which conditions, and what remains unknown.

## Immediate Next Task

Implement `TASK-28` next because physical-device validation observed an
incorrectly classified background interruption. Then implement `TASK-51`,
`TASK-52`, and `TASK-53` to expose secondary diagnostics, make the primary
surfaces system-bar safe, and remove known Compose log noise on current Android
releases. Continue with `TASK-22` before expanding API families:

> Preserve the foreground-only AICore boundary honestly, expose inspectable
> diagnostics without cluttering the primary flow, and export reproducibility
> evidence before adding more capability families.

This is the highest-leverage next step because the observed background failure
currently produces misleading recovery guidance, while the stored evidence
cannot yet be inspected or exported conveniently.
