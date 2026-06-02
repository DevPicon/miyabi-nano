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
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidExperimentContextProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ExperimentContextProvider {
    private val runSequence = AtomicInteger()

    override fun capture(input: ExperimentContextInput): ExperimentContext =
        ExperimentContext(
            appVersion = appVersion(),
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            androidBuild = Build.DISPLAY,
            apiLevel = Build.VERSION.SDK_INT,
            baseModelName = input.baseModelName,
            featureStatusBeforeRun = input.featureStatusBeforeRun.name,
            connectivity = connectivity(),
            powerState = powerState(),
            thermalStatus = thermalStatus(),
            runSequence = runSequence.incrementAndGet(),
            heuristicInputSize = input.heuristicInputSize,
            outcomeCategory = input.outcomeCategory
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
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> "ONLINE_OTHER"
            else -> "OFFLINE"
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
