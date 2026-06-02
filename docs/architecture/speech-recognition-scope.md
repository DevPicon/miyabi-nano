# Speech Recognition Scope Decision

## Decision

Do not add speech recognition to the Gemini Nano capability registry.

The current [ML Kit GenAI overview](https://developers.google.com/ml-kit/genai)
lists Summarization, Proofreading, Rewriting, Image Description, and Prompt.
It does not list Speech Recognition as an ML Kit GenAI or AICore capability.

Android does provide an on-device speech path through
[`SpeechRecognizer.createOnDeviceSpeechRecognizer()`](https://developer.android.com/reference/android/speech/SpeechRecognizer#createOnDeviceSpeechRecognizer(android.content.Context)).
That API is a separate platform service boundary and should not inherit Gemini
Nano claims, ML Kit readiness states, AICore quotas, or the capability registry.

## Separate Experiment Requirements

If a future portfolio task adds Android on-device speech recognition, it must
define a separate lane and account for:

- `RECORD_AUDIO` permission and denial UX
- `SpeechRecognizer.isOnDeviceRecognitionAvailable()`
- main-thread-only method calls
- `RecognitionListener` callback lifecycle
- `destroy()` ownership
- language support and model-download APIs
- interruption, microphone contention, and background behavior
- the distinction between on-device and potentially server-backed recognizers

## Portfolio Boundary

Speech recognition remains a useful Android platform experiment, but it is not
evidence about AICore or Gemini Nano. Keeping it separate protects the technical
credibility of `miyabi-nano`.
