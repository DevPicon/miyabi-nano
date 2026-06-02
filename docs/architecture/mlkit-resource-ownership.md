# ML Kit Resource Ownership

ML Kit GenAI clients are closeable resources. The application must not retain a
closed client or depend on process termination for cleanup.

## Ownership Boundaries

| Owner | Clients | Creation | Release |
| --- | --- | --- | --- |
| `ModelDownloadViewModel` | One client per visible capability | ViewModel construction | `onCleared()` |
| `InferenceUseCase` invocation | One client for the selected capability | Flow collection begins | `finally` after blocked, success, failure, or cancellation |

`CapabilityPreparationClientFactory` is application-scoped, but the SDK clients
it creates are not. This allows each lifecycle owner to recreate a fresh client
instead of accidentally reusing a closed singleton.

## Trade-off

Creating an inference client per invocation is intentionally conservative. It
provides deterministic ownership before performance optimization. Future
benchmark work may measure the cost and propose a longer-lived owner only if it
preserves an explicit close-and-recreate policy.
