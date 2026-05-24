package uz.mentorai.focus.core.permissions

import android.Manifest
import android.app.AppOpsManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.ContextCompat
import uz.mentorai.focus.guard.AppMonitorAccessibility
import uz.mentorai.focus.guard.MentorDeviceAdmin

/**
 * Har bir permission'ning hozirgi holatini tekshiradi va
 * uni so'rash uchun kerakli Intent'ni qaytaradi.
 */
object PermissionChecker {

    fun isGranted(context: Context, step: PermissionStep): Boolean = when (step) {
        PermissionStep.NOTIFICATIONS ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true

        PermissionStep.USAGE_STATS -> hasUsageStatsAccess(context)

        PermissionStep.OVERLAY -> Settings.canDrawOverlays(context)

        PermissionStep.ACCESSIBILITY -> isAccessibilityServiceEnabled(context)

        PermissionStep.DEVICE_ADMIN -> {
            val dpm = context.getSystemService(DevicePolicyManager::class.java)
            dpm.isAdminActive(MentorDeviceAdmin.componentName(context))
        }

        PermissionStep.AUDIO_RECORDING ->
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasUsageStatsAccess(context: Context): Boolean {
        val ops = context.getSystemService(AppOpsManager::class.java)
        val mode = ops.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val expected = "${context.packageName}/${AppMonitorAccessibility::class.java.name}"
        val enabled = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val splitter = TextUtils.SimpleStringSplitter(':')
        splitter.setString(enabled)
        return splitter.any { it.equals(expected, ignoreCase = true) }
    }

    fun runtimePermissionFor(step: PermissionStep): String? = when (step) {
        PermissionStep.NOTIFICATIONS ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.POST_NOTIFICATIONS else null
        PermissionStep.AUDIO_RECORDING -> Manifest.permission.RECORD_AUDIO
        else -> null
    }

    fun settingsIntent(context: Context, step: PermissionStep): android.content.Intent? = when (step) {
        PermissionStep.USAGE_STATS ->
            android.content.Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)

        PermissionStep.OVERLAY ->
            android.content.Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )

        PermissionStep.ACCESSIBILITY ->
            android.content.Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)

        PermissionStep.DEVICE_ADMIN ->
            android.content.Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    MentorDeviceAdmin.componentName(context)
                )
                putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Mentor seni o'zingdan himoya qilishi uchun bu kerak."
                )
            }

        else -> null
    }
}
