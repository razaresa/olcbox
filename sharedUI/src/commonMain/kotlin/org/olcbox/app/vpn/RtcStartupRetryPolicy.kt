package org.olcbox.app.vpn

internal object RtcStartupRetryPolicy {
    fun shouldRetry(message: String?, attempt: Int, maxAttempts: Int): Boolean {
        if (attempt >= maxAttempts) return false
        val lowerMessage = message.orEmpty().lowercase()
        return lowerMessage.contains("read welcome") ||
            lowerMessage.contains("unexpected handshake message") ||
            lowerMessage.contains("control_ping")
    }
}
