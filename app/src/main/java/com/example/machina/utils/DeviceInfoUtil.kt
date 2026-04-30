package com.example.machina.utils
import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.example.machina.data.model.dashboard_models.DeviceInfo
import java.io.File

object DeviceInfoUtil {

    fun getDeviceInfo(context: Context): DeviceInfo {

        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val statFs = StatFs(Environment.getDataDirectory().path)

        val totalStorage = statFs.blockSizeLong * statFs.blockCountLong
        val freeStorage = statFs.blockSizeLong * statFs.availableBlocksLong
        val usedStorage = totalStorage - freeStorage

        return DeviceInfo(
            totalRam = formatSize(memoryInfo.totalMem),
            availableRam = formatSize(memoryInfo.availMem),
            totalStorage = formatSize(totalStorage),
            freeStorage = formatSize(freeStorage),
            usedStorage = formatSize(usedStorage),
            cpuCores = Runtime.getRuntime().availableProcessors(),
            cpuInfoRaw = getCpuInfo()
        )
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