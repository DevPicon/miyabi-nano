package dev.picon.android.miyabinano.data

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MetricsRepositoryTest {
    @Test
    fun saveMetrics_replacesRecordWithPersistenceDuration() = runBlocking {
        val dao = FakeMetricsDao()
        val repository = MetricsRepository(dao)
        val metrics = metrics()

        val saved = repository.saveMetrics(metrics)

        assertEquals(2, dao.inserted.size)
        assertEquals(metrics.id, dao.inserted.first().id)
        assertEquals(metrics.id, dao.inserted.last().id)
        assertNotNull(saved.timingMilestones.persistenceMs)
        assertEquals(saved.toEntity(), dao.inserted.last())
    }

    private fun metrics() = InferenceMetrics(
        capability = InferenceCapability.SUMMARIZATION,
        inputText = "input",
        inputTokenCount = 1,
        inputCharCount = 5,
        outputText = "output",
        outputTokenCount = 1,
        outputCharCount = 6,
        modelLoadTimeMs = 1,
        inferenceTimeMs = 2,
        totalTimeMs = 3,
        processHeapDeltaMB = 0,
        runtimeMaxHeapMB = 256
    )

    private class FakeMetricsDao : MetricsDao {
        val inserted = mutableListOf<MetricsEntity>()

        override fun getAllMetrics(): Flow<List<MetricsEntity>> = flowOf(inserted)

        override fun getMetricsByCapability(capability: String): Flow<List<MetricsEntity>> =
            flowOf(inserted.filter { it.capability == capability })

        override suspend fun getMetricsById(id: String): MetricsEntity? =
            inserted.lastOrNull { it.id == id }

        override suspend fun insertMetrics(metrics: MetricsEntity) {
            inserted += metrics
        }

        override suspend fun deleteMetricsById(id: String) = Unit

        override suspend fun deleteAllMetrics() = Unit

        override suspend fun getMetricsCount(): Int = inserted.size
    }
}
