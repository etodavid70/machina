package com.example.machina.utils
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.example.machina.data.model.dashboard_models.DeviceInfo
import java.io.File

object DeviceInfoUtil {
    private const val MIN_TOTAL_RAM_BYTES = 4L * 1024L * 1024L * 1024L
    private const val MIN_FREE_STORAGE_BYTES = 20L * 1024L * 1024L * 1024L
    private const val MIN_CPU_CORES = 4
    private const val MIN_CPU_MAX_FREQUENCY_MHZ = 1800

    private val cpuDirectoryNameRegex = Regex("cpu\\d+")

    fun getDeviceInfo(context: Context): DeviceInfo {

        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val statFs = StatFs(Environment.getDataDirectory().path)

        val totalStorage = statFs.blockSizeLong * statFs.blockCountLong
        val freeStorage = statFs.blockSizeLong * statFs.availableBlocksLong
        val usedStorage = totalStorage - freeStorage
        val cpuCores = Runtime.getRuntime().availableProcessors()
        val cpuMaxFrequencyMhz = getMaxCpuFrequencyMhz()
        val has64BitCpu = Build.SUPPORTED_64_BIT_ABIS.isNotEmpty()
        val hasMinimumRam = memoryInfo.totalMem >= MIN_TOTAL_RAM_BYTES
        val hasMinimumStorage = freeStorage >= MIN_FREE_STORAGE_BYTES
        val hasMinimumCpuFrequency =
            cpuMaxFrequencyMhz == null || cpuMaxFrequencyMhz >= MIN_CPU_MAX_FREQUENCY_MHZ
        val hasStrongEnoughCpu =
            has64BitCpu && cpuCores >= MIN_CPU_CORES && hasMinimumCpuFrequency
        val requirementFailures = getRequirementFailures(
            hasMinimumRam = hasMinimumRam,
            hasMinimumStorage = hasMinimumStorage,
            has64BitCpu = has64BitCpu,
            cpuCores = cpuCores,
            cpuMaxFrequencyMhz = cpuMaxFrequencyMhz,
            hasMinimumCpuFrequency = hasMinimumCpuFrequency
        )

        return DeviceInfo(
            totalRam = formatSize(memoryInfo.totalMem),
            availableRam = formatSize(memoryInfo.availMem),
            totalRamBytes = memoryInfo.totalMem,
            availableRamBytes = memoryInfo.availMem,
            totalStorage = formatSize(totalStorage),
            freeStorage = formatSize(freeStorage),
            usedStorage = formatSize(usedStorage),
            totalStorageBytes = totalStorage,
            freeStorageBytes = freeStorage,
            usedStorageBytes = usedStorage,
            cpuCores = cpuCores,
            cpuMaxFrequencyMhz = cpuMaxFrequencyMhz,
            has64BitCpu = has64BitCpu,
            hasMinimumRam = hasMinimumRam,
            hasMinimumStorage = hasMinimumStorage,
            hasStrongEnoughCpu = hasStrongEnoughCpu,
            canCreateVirtualMachine = requirementFailures.isEmpty(),
            requirementFailures = requirementFailures,
            cpuInfoRaw = getCpuInfo()
        )
    }

    private fun getRequirementFailures(
        hasMinimumRam: Boolean,
        hasMinimumStorage: Boolean,
        has64BitCpu: Boolean,
        cpuCores: Int,
        cpuMaxFrequencyMhz: Int?,
        hasMinimumCpuFrequency: Boolean
    ): List<String> {
        val failures = mutableListOf<String>()

        if (!hasMinimumRam) {
            failures.add("Total RAM must be at least 4 GB.")
        }

        if (!hasMinimumStorage) {
            failures.add("Available storage must be at least 20 GB.")
        }

        if (!has64BitCpu) {
            failures.add("CPU must support 64-bit Android.")
        }

        if (cpuCores < MIN_CPU_CORES) {
            failures.add("CPU must have at least $MIN_CPU_CORES cores.")
        }

        if (!hasMinimumCpuFrequency && cpuMaxFrequencyMhz != null) {
            failures.add("CPU max frequency must be at least ${MIN_CPU_MAX_FREQUENCY_MHZ} MHz.")
        }

        return failures
    }

    private fun getMaxCpuFrequencyMhz(): Int? {
        return try {
            File("/sys/devices/system/cpu")
                .listFiles()
                ?.filter { it.isDirectory && cpuDirectoryNameRegex.matches(it.name) }
                ?.mapNotNull { cpuDirectory ->
                    File(cpuDirectory, "cpufreq/cpuinfo_max_freq")
                        .takeIf { it.exists() }
                        ?.readText()
                        ?.trim()
                        ?.toLongOrNull()
                }
                ?.maxOrNull()
                ?.let { (it / 1000L).toInt() }
        } catch (e: Exception) {
            null
        }
    }

    private fun getCpuInfo(): String {
        return try {
            File("/proc/cpuinfo").readText()
        } catch (e: Exception) {
            "Error reading CPU info: ${e.message}"
        }
    }

    private fun formatSize(size: Long): String {
        val kb = size / 1024
        val mb = kb / 1024
        val gb = mb / 1024

        return when {
            gb > 0 -> "$gb GB"
            mb > 0 -> "$mb MB"
            else -> "$kb KB"
        }
    }
}
