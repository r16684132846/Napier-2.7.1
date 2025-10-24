package io.github.aakira.napier

import kotlin.test.Test
import kotlin.test.assertTrue

class NapierOhosTest {

    @Test
    fun testOhosLogging() {
        var logged = false
        val testAntilog = object : Antilog() {
            override fun performLog(
                priority: LogLevel,
                tag: String?,
                throwable: Throwable?,
                message: String?
            ) {
                logged = true
            }
        }

        Napier.base(testAntilog)
        Napier.i("Test message")
        assertTrue(logged)
        Napier.takeLogarithm(testAntilog)
    }
}
