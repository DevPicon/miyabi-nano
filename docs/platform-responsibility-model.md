# Android OS-Supported AI Responsibility Model

The implementation and educational content must distinguish vendor-provided
runtime behavior from application responsibilities.

## Android / AICore Responsibilities

- Execute supported Gemini Nano inference on-device.
- Share the system-managed base model across applications where available.
- Apply device- and model-version-dependent constraints.
- Enforce foreground-only inference and per-app quotas.
- Surface feature readiness and download lifecycle through ML Kit.

## Application Responsibilities

- Check each configured capability at runtime.
- Explain unsupported, downloadable, downloading, and available states.
- Provide honest provisioning and offline UX.
- Handle typed failures and retries without promising guaranteed availability.
- Cancel or reconcile work when the app loses foreground eligibility.
- Measure cold and warm behavior with enough context to reproduce results.
- Minimize, disclose, and control storage of user text.
- Document realistic use-case boundaries rather than implying general-purpose
  local LLM capability.

## Official References

- [ML Kit GenAI API overview](https://developers.google.com/ml-kit/genai)
- [Gemini Nano in Android AICore](https://developer.android.com/ai/aicore)
- [Summarization API](https://developers.google.com/ml-kit/genai/summarization/android)
- [Proofreading API](https://developers.google.com/ml-kit/genai/proofreading/android)
- [Rewriting API](https://developers.google.com/ml-kit/genai/rewriting/android)
- [GenAI error codes](https://developers.google.com/android/reference/com/google/mlkit/genai/common/GenAiException.ErrorCode)
