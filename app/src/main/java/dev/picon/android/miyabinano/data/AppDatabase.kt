package dev.picon.android.miyabinano.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MetricsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metricsDao(): MetricsDao

    companion object {
        const val DATABASE_NAME = "miyabi_nano_db"
    }
}
