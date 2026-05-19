package org.olcbox.app.vpn

import org.olcbox.app.data.model.LocationConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JitsiRoomFailoverSelectorTest {

    @Test
    fun selectsPrimaryThenAdvancesAcrossJitsiRooms() {
        val primary = "https://meet.cryptopro.ru/olcrtc-room"
        val backup = "https://jitsi.etudevs.ru/olcrtc-room"
        val location = LocationConfig(
            name = "Jitsi",
            id = "$primary,$backup",
            key = "b".repeat(64),
            bypassProvider = LocationConfig.PROVIDER_JITSI,
            transport = LocationConfig.TRANSPORT_DATACHANNEL
        )
        val selector = JitsiRoomFailoverSelector()

        assertEquals(primary, selector.select(location).id)
        assertTrue(selector.advance(location))
        assertEquals(backup, selector.select(location).id)
        assertTrue(selector.advance(location))
        assertEquals(primary, selector.select(location).id)
    }

    @Test
    fun ignoresNonJitsiCommaRoomIds() {
        val location = LocationConfig(
            name = "WB",
            id = "room-a,room-b",
            key = "b".repeat(64),
            bypassProvider = LocationConfig.PROVIDER_WB_STREAM
        )
        val selector = JitsiRoomFailoverSelector()

        assertEquals("room-a,room-b", selector.select(location).id)
        assertFalse(selector.advance(location))
    }

    @Test
    fun reportsFailoverOnlyForMultiRoomJitsi() {
        val selector = JitsiRoomFailoverSelector()
        val jitsiMultiRoom = LocationConfig(
            name = "Jitsi",
            id = "https://meet.cryptopro.ru/olcrtc-room,https://jitsi.etudevs.ru/olcrtc-room",
            key = "b".repeat(64),
            bypassProvider = LocationConfig.PROVIDER_JITSI,
            transport = LocationConfig.TRANSPORT_DATACHANNEL
        )
        val jitsiSingleRoom = jitsiMultiRoom.copy(id = "https://meet.cryptopro.ru/olcrtc-room")
        val nonJitsiMultiRoom = jitsiMultiRoom.copy(
            id = "room-a,room-b",
            bypassProvider = LocationConfig.PROVIDER_WB_STREAM
        )

        assertTrue(selector.canFailover(jitsiMultiRoom))
        assertFalse(selector.canFailover(jitsiSingleRoom))
        assertFalse(selector.canFailover(nonJitsiMultiRoom))
    }
}
