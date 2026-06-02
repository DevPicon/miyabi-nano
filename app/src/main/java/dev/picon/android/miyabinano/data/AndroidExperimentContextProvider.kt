package dev.picon.android.miyabinano.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.picon.android.miyabinano.domain.model.ExperimentContext
import dev.picon.android.miyabinano.domain.model.ExperimentContextInput
import dev.picon.android.miyabinano.domain.model.ExperimentContextProvider
import dev.picon.android.miyabinano.domain.model.ExperimentSchema
import dev.picon.android.miyabinano.domain.model.AppDiagnostics
import dev.picon.android.miyabinano.domain.model.AppDiagnosticsProvider
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidExperimentContextProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ExperimentContextProvider, AppDiagnosticsProvider {
    private val runSequence = AtomicInteger()

    override fun capture(input: ExperimentContextInput): ExperimentContext {
        val diagnostics = capture()
        return ExperimentContext(
            appVersion = diagnostics.appVersion,
            deviceManufacturer = diagnostics.deviceManufacturer,
            deviceModel = diagnostics.deviceModel,
            androidBuild = diagnostics.androidBuild,
            apiLevel = diagnostics.apiLevel,
            baseModelName = input.baseModelName,
            featureStatusBeforeRun = input.featureStatusBeforeRun.name,
            connectivity = diagnostics.connectivity,
            powerState = diagnostics.powerState,
            thermalStatus = diagnostics.thermalStatus,
            runSequence = runSequence.incrementAndGet(),
            fixtureId = input.fixtureId,
            heuristicInputSize = input.heuristicInputSize,
            outcomeCategory = input.outcomeCategory
        )
    }

    override fun capture(): AppDiagnostics =
        AppDiagnostics(
            appVersion = appVersion(),
            databaseVersion = AppDatabase.DATABASE_VERSION,
            experimentSchemaVersion = ExperimentSchema.CURRENT_VERSION,
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            androidBuild = Build.DISPLAY,
            apiLevel = Build.VERSION.SDK_INT,
            connectivity = connectivity(),
            powerState = powerState(),
            thermalStatus = thermalStatus()
        )

    private fun appVersion(): String = annotation {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
            ?: ExperimentSchema.UNKNOWN
    }

    private fun connectivity(): String = annotation {
        val manager = context.getSystemService(ConnectivityManager::class.java)
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            ?: return@annotation "OFFLINE"
        when {
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> "OFFLINE"
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ->
                "ONLINE_UNVALIDATED"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            else -> "ONLINE_OTHER"
        }
    }

    private fun powerState(): String = annotation {
        val battery = context.getSystemService(BatteryManager::class.java)
        val percentage = battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        "charging=${battery.isCharging},batteryPercent=$percentage"
    }

    private fun thermalStatus(): String = annotation {
        val power = context.getSystemService(PowerManager::class.java)
        power.currentThermalStatus.toString()
    }

    private inline fun annotation(block: () -> String): String =
        runCatching(block).getOrDefault(ExperimentSchema.UNKNOWN)
}
