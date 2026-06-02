package dev.picon.android.miyabinano.domain.model

data class AppDiagnostics(
    val appVersion: String,
    val databaseVersion: Int,
    val experimentSchemaVersion: Int,
    val deviceManufacturer: String,
    val deviceModel: String,
    val androidBuild: String,
    val apiLevel: Int,
    val connectivity: String,
    val powerState: String,
    val thermalStatus: String
)

interface AppDiagnosticsProvider {
    fun capture(): AppDiagnostics
}
