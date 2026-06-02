package dev.picon.android.miyabinano.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [MetricsEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metricsDao(): MetricsDao

    companion object {
        const val DATABASE_NAME = "miyabi_nano_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN schemaVersion INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN appVersion TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN deviceManufacturer TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN deviceModel TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN androidBuild TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN apiLevel INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN baseModelName TEXT")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN featureStatusBeforeRun TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN connectivity TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN powerState TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN thermalStatus TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN runSequence INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN runClassification TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN fixtureId TEXT")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN heuristicInputSize INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN outcomeCategory TEXT NOT NULL DEFAULT 'UNKNOWN'")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN preparationWaitMs INTEGER")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN downloadDurationMs INTEGER")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN firstVisibleOutputMs INTEGER")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN inferenceCompletionMs INTEGER")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN persistenceMs INTEGER")
                db.execSQL("ALTER TABLE inference_metrics ADD COLUMN userPerceivedTotalMs INTEGER")
            }
        }
    }
}
