package dev.picon.android.miyabinano.ui.components

import dev.picon.android.miyabinano.domain.genai.BaseModelIdentityState
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState

fun CapabilityPreparationState.diagnosticsLabel(): String =
    when (this) {
        CapabilityPreparationState.Checking -> "Checking"
        CapabilityPreparationState.Unavailable -> "Unavailable"
        CapabilityPreparationState.Downloadable -> "Downloadable"
        is CapabilityPreparationState.Downloading -> "Downloading"
        CapabilityPreparationState.Available -> "Available"
        is CapabilityPreparationState.Failed -> "Failed: ${failure.category}"
    }

fun BaseModelIdentityState.exposedNameOrNull(): String? =
    (this as? BaseModelIdentityState.Available)?.name

fun BaseModelIdentityState.failureDetailOrNull(): String? =
    (this as? BaseModelIdentityState.Failed)?.failure?.technicalDetail

fun CapabilityPreparationState.failureDetailOrNull(): String? =
    (this as? CapabilityPreparationState.Failed)?.failure?.technicalDetail
