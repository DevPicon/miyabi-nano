# AICore Failure Taxonomy

The application maps ML Kit `GenAiException` values to stable app-owned
categories before failures reach UI or future experiment persistence.

| Application category | ML Kit source | Recovery boundary |
| --- | --- | --- |
| `SERVICE_DISCONNECTED` | Observed nested `IPC_ERROR` service-disconnect detail | Keep network access, wait, retry |
| `AICORE_INCOMPATIBLE` | `AICORE_INCOMPATIBLE` | Update system AI components |
| `NEEDS_SYSTEM_UPDATE` | `NEEDS_SYSTEM_UPDATE` | Update Android |
| `FEATURE_UNAVAILABLE` | `NOT_AVAILABLE` | Choose another experiment or retry after system updates |
| `DISK_PRESSURE` | `NOT_ENOUGH_DISK_SPACE` | Free storage, retry |
| `BUSY` | `BUSY` | Retry with backoff |
| `CANCELLED` | `CANCELLED` | Retry only if still needed |
| `BACKGROUND_USE_BLOCKED` | Documented code `30` | Return app to foreground, retry |
| `BATTERY_QUOTA_EXCEEDED` | Documented code `27` | Retry after quota recovery |
| `INPUT_TOO_LARGE` | `REQUEST_TOO_LARGE` | Shorten input |
| `INPUT_TOO_SMALL` | `REQUEST_TOO_SMALL` | Lengthen input |
| `POLICY_REJECTION` | Request or response processing and generation policy errors | Adjust input |
| `UNKNOWN` | Any unrecognized outcome | Retain technical detail and retry cautiously |

The installed `com.google.mlkit:genai-common:1.0.0-beta1` artifact does not
expose compile-time constants for the documented foreground-blocking and
battery-quota codes. The adapter maps their documented numeric values so newer
runtime outcomes remain distinguishable while the dependency is still beta.

Source: [ML Kit `GenAiException.ErrorCode` reference](https://developers.google.com/android/reference/com/google/mlkit/genai/common/GenAiException.ErrorCode)
