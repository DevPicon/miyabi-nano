package dev.picon.android.miyabinano.data

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetricsRepository @Inject constructor(
    private val metricsDao: MetricsDao
) {
    fun getAllMetrics(): Flow<List<InferenceMetrics>> =
        metricsDao.getAllMetrics().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getMetricsByCapability(capability: InferenceCapability): Flow<List<InferenceMetrics>> =
        metricsDao.getMetricsByCapability(capability.name).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getMetricsById(id: String): InferenceMetrics? =
        metricsDao.getMetricsById(id)?.toDomain()

    suspend fun saveMetrics(metrics: InferenceMetrics): InferenceMetrics {
        val startNanos = System.nanoTime()
        metricsDao.insertMetrics(metrics.toEntity())
        val persistenceMs = (System.nanoTime() - startNanos) / 1_000_000
        val updatedMetrics = metrics.copy(
            timingMilestones = metrics.timingMilestones.copy(
                persistenceMs = persistenceMs,
                userPerceivedTotalMs = metrics.timingMilestones.userPerceivedTotalMs
                    ?: metrics.totalTimeMs
            )
        )
        metricsDao.insertMetrics(updatedMetrics.toEntity())
        return updatedMetrics
    }

    suspend fun deleteMetricsById(id: String) {
        metricsDao.deleteMetricsById(id)
    }

    suspend fun deleteAllMetrics() {
        metricsDao.deleteAllMetrics()
    }

    suspend fun getMetricsCount(): Int =
        metricsDao.getMetricsCount()
}
