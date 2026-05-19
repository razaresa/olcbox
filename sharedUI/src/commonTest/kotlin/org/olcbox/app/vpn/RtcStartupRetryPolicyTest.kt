package org.olcbox.app.vpn

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RtcStartupRetryPolicyTest {

    @Test
    fun retriesFirstHandshakeTimeout() {
        assertTrue(
            RtcStartupRetryPolicy.shouldRetry(
                message = "handshake: handshake client: read welcome: read hdr: timeout",
                attempt = 1,
                maxAttempts = 2
            )
        )
    }

    @Test
    fun doesNotRetryAfterMaxAttempt() {
        assertFalse(
            RtcStartupRetryPolicy.shouldRetry(
                message = "handshake client: read welcome: read hdr: timeout",
                attempt = 2,
                maxAttempts = 2
            )
        )
    }

    @Test
    fun doesNotRetryGenericStartTimeout() {
        assertFalse(
            RtcStartupRetryPolicy.shouldRetry(
                message = "olcRTC start timed out",
                attempt = 1,
                maxAttempts = 2
            )
        )
    }
}
