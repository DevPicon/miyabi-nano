package dev.picon.android.miyabinano.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MetricsDao {
    @Query("SELECT * FROM inference_metrics ORDER BY timestamp DESC")
    fun getAllMetrics(): Flow<List<MetricsEntity>>

    @Query("SELECT * FROM inference_metrics WHERE capability = :capability ORDER BY timestamp DESC")
    fun getMetricsByCapability(capability: String): Flow<List<MetricsEntity>>

    @Query("SELECT * FROM inference_metrics WHERE id = :id")
    suspend fun getMetricsById(id: String): MetricsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetrics(metrics: MetricsEntity)

    @Query("DELETE FROM inference_metrics WHERE id = :id")
    suspend fun deleteMetricsById(id: String)

    @Query("DELETE FROM inference_metrics")
    suspend fun deleteAllMetrics()

    @Query("SELECT COUNT(*) FROM inference_metrics")
    suspend fun getMetricsCount(): Int
}
