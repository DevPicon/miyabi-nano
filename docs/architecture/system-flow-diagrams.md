# Miyabi Nano System Flow Diagrams

## Responsibility Boundary

```mermaid
flowchart LR
    subgraph APP["Application-owned"]
        UX["Readiness and recovery UX"]
        ORCH["Inference orchestration"]
        DIAG["Diagnostics and evidence"]
        PRIV["Local persistence policy"]
    end

    subgraph OS["Android / AICore-owned"]
        READY["Capability readiness"]
        PROVISION["Asset provisioning"]
        SAFETY["Request processing and safety"]
        MODEL["Gemini Nano model lifecycle"]
        ACCEL["Hardware acceleration"]
    end

    UX --> READY
    ORCH --> SAFETY
    DIAG --> READY
    READY --> PROVISION
    PROVISION --> MODEL
    SAFETY --> MODEL
    MODEL --> ACCEL
    PRIV -. "remains app responsibility" .-> DIAG
```

## Capability Provisioning

```mermaid
stateDiagram-v2
    [*] --> Checking
    Checking --> Unavailable: unsupported configuration
    Checking --> Downloadable: required asset missing
    Checking --> Downloading: provisioning already active
    Checking --> Available: configured capability ready
    Downloadable --> Downloading: user starts setup
    Downloading --> Available: download completes
    Downloading --> Failed: disconnect, disk pressure, or other error
    Failed --> Checking: retry
    Available --> Checking: refresh status
```

## Inference Attempt

```mermaid
sequenceDiagram
    actor User
    participant UI as Compose UI
    participant VM as InferenceViewModel
    participant UseCase as InferenceUseCase
    participant Client as ML Kit capability adapter
    participant AICore as Android AICore

    User->>UI: Select fixture and run
    UI->>VM: onRunInference()
    VM->>Client: requestSnapshot(input)
    Note over VM,Client: Retain public SDK request boundary for diagnostics
    VM->>UseCase: invoke(capability, input, fixtureId)
    UseCase->>Client: create fresh configured client
    Client->>AICore: checkFeatureStatus()
    AICore-->>Client: AVAILABLE or blocked state
    Client->>AICore: prepareInferenceEngine()
    Client->>AICore: runInference(public request)
    AICore-->>Client: output or typed failure
    Client-->>UseCase: mapped app-owned result
    UseCase->>Client: close()
    UseCase-->>VM: output, metrics, or failure
    VM-->>UI: render result and diagnostics
```

## Summarization Recovery Investigation

The repository observed Summarization recovering after uninstall and reinstall.
The smallest required recovery boundary remains unknown.

```mermaid
flowchart TD
    FAIL["Repeated Summarization generation failure"] --> INPUT["Try a different valid article"]
    INPUT -->|still fails| NAV["Navigate away and reopen experiment"]
    NAV -->|still fails| RESTART["Force-stop and relaunch app"]
    RESTART -->|still fails| CLEAR["Clear app data"]
    CLEAR -->|still fails| REINSTALL["Uninstall and reinstall"]
    INPUT -->|works| RECORD1["Record input-specific failure"]
    NAV -->|works| RECORD2["Record screen or ViewModel recovery boundary"]
    RESTART -->|works| RECORD3["Record process recovery boundary"]
    CLEAR -->|works| RECORD4["Record app-data recovery boundary"]
    REINSTALL -->|works| RECORD5["Record reinstall recovery boundary"]
```

Do not add a speculative reset-model button. ML Kit exposes configured client
close and recreation, but not a public model-reset API.
