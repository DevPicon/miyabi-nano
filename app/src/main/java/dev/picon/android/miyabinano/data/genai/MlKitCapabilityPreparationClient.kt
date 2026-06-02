package dev.picon.android.miyabinano.data.genai

import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClient
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationException
import dev.picon.android.miyabinano.domain.genai.CapabilityProvisioningEvent
import dev.picon.android.miyabinano.domain.genai.CapabilityReadiness
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.CancellationException

class MlKitCapabilityPreparationClient(
    override val capability: InferenceCapability,
    private val checkFeatureStatus: suspend () -> Int,
    private val downloadFeature: suspend (DownloadCallback) -> Unit,
    private val prepareEngine: suspend () -> Unit,
    private val baseModelName: suspend () -> String,
    private val inference: suspend (String) -> String,
    private val closeClient: () -> Unit
) : CapabilityPreparationClient {
    override suspend fun checkReadiness(): CapabilityReadiness =
        when (checkFeatureStatus()) {
            FeatureStatus.UNAVAILABLE -> CapabilityReadiness.UNAVAILABLE
            FeatureStatus.DOWNLOADABLE -> CapabilityReadiness.DOWNLOADABLE
            FeatureStatus.DOWNLOADING -> CapabilityReadiness.DOWNLOADING
            FeatureStatus.AVAILABLE -> CapabilityReadiness.AVAILABLE
            else -> CapabilityReadiness.UNKNOWN
        }

    override suspend fun provision(onEvent: (CapabilityProvisioningEvent) -> Unit) {
        try {
            downloadFeature(
                object : DownloadCallback {
                override fun onDownloadStarted(bytesToDownload: Long) {
                    onEvent(CapabilityProvisioningEvent.Started(bytesToDownload))
                }

                override fun onDownloadProgress(totalBytesDownloaded: Long) {
                    onEvent(CapabilityProvisioningEvent.Progress(totalBytesDownloaded))
                }

                override fun onDownloadCompleted() {
                    onEvent(CapabilityProvisioningEvent.Completed)
                }

                    override fun onDownloadFailed(e: GenAiException) {
                        onEvent(CapabilityProvisioningEvent.Failed(MlKitCapabilityFailureMapper.map(e)))
                    }
                }
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw CapabilityPreparationException(MlKitCapabilityFailureMapper.map(e), e)
        }
    }

    override suspend fun prepareInferenceEngine() {
        prepareEngine()
    }

    override suspend fun getBaseModelName(): String = baseModelName()

    override suspend fun runInference(inputText: String): String = inference(inputText)

    override fun close() {
        closeClient()
    }
}
