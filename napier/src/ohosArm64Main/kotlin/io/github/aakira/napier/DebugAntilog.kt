package io.github.aakira.napier

import platform.ohos.hilog.*

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
            // 使用OpenHarmony原生HiLog
            val label = HiLogLabel(HiLog.LOG_APP, DOMAIN, logTag)

            when (priority) {
                LogLevel.VERBOSE -> HiLog.debug(label, "%{public}s", fullMessage)
                LogLevel.DEBUG -> HiLog.debug(label, "%{public}s", fullMessage)
                LogLevel.INFO -> HiLog.info(label, "%{public}s", fullMessage)
                LogLevel.WARNING -> HiLog.warn(label, "%{public}s", fullMessage)
                LogLevel.ERROR -> HiLog.error(label, "%{public}s", fullMessage)
                LogLevel.ASSERT -> HiLog.fatal(label, "%{public}s", fullMessage)
            }
        } catch (e: Exception) {
            // 防止日志记录异常影响主流程
            println("Failed to log message: ${e.message}")
        }
    }
}
