package io.github.aakira.napier

import kotlinx.cinterop.ExperimentalForeignApi
import platform.ohos.LOG_DEBUG
import platform.ohos.LOG_ERROR
import platform.ohos.LOG_FATAL
import platform.ohos.LOG_INFO
import platform.ohos.LOG_WARN
import platform.ohos.hilog.*

@OptIn(ExperimentalForeignApi::class)


actual class DebugAntilog actual constructor(private val defaultTag: String) : Antilog() {
    companion object {
        private const val DOMAIN = 0x00201
    }

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val logTag = tag ?: defaultTag

        val fullMessage = when {
            message != null && throwable != null -> "$message\n${throwable.message}"
            message != null -> message
            throwable != null -> throwable.message ?: return
            else -> return
        }

        try {
            val logType = when (priority) {
                LogLevel.VERBOSE -> LOG_DEBUG.toUInt()
                LogLevel.DEBUG -> LOG_DEBUG.toUInt()
                LogLevel.INFO -> LOG_INFO.toUInt()
                LogLevel.WARNING -> LOG_WARN.toUInt()
                LogLevel.ERROR -> LOG_ERROR.toUInt()
                LogLevel.ASSERT -> LOG_FATAL.toUInt()
            }

            OH_LOG_Print_Helper(logType, DOMAIN.toUInt(), logTag, "%{public}s", fullMessage)
        } catch (e: Exception) {
            // 防止日志记录异常影响主流程
            println("Failed to log message: ${e.message}")
        }
    }
}
