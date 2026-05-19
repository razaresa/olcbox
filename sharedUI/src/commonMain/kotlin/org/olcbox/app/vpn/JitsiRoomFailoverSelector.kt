package org.olcbox.app.vpn

import org.olcbox.app.data.model.LocationConfig

class JitsiRoomFailoverSelector {
    private var candidateKey = ""
    private var candidateIndex = 0

    fun select(location: LocationConfig): LocationConfig {
        val config = location.normalized()
        val candidates = candidatesFor(config)
        syncCandidates(candidates)
        val room = candidates.getOrNull(candidateIndex) ?: config.id
        return config.copy(id = room)
    }

    fun advance(location: LocationConfig): Boolean {
        val candidates = candidatesFor(location.normalized())
        syncCandidates(candidates)
        if (candidates.size <= 1) return false
        candidateIndex = (candidateIndex + 1) % candidates.size
        return true
    }

    fun canFailover(location: LocationConfig): Boolean {
        return candidatesFor(location.normalized()).size > 1
    }

    fun reset() {
        candidateKey = ""
        candidateIndex = 0
    }

    private fun candidatesFor(config: LocationConfig): List<String> {
        if (config.bypassProvider != LocationConfig.PROVIDER_JITSI) {
            return listOf(config.id)
        }
        return config.roomCandidates().ifEmpty { listOf(config.id) }
    }

    private fun syncCandidates(candidates: List<String>) {
        val key = candidates.joinToString("\u001F")
        if (key != candidateKey) {
            candidateKey = key
            candidateIndex = 0
        }
    }
}
