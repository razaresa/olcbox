package org.olcbox.app.vpn

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VpnRoutePolicyTest {

    @Test
    fun publicRoutesKeepCommonPublicAndMapDnsAddressesInTun() {
        val routes = VpnRoutePolicy.publicIpv4RoutesExcludingPrivateLan

        assertTrue(VpnRoutePolicy.contains(routes, "1.1.1.1"))
        assertTrue(VpnRoutePolicy.contains(routes, "8.8.8.8"))
        assertTrue(VpnRoutePolicy.contains(routes, "100.64.0.10"))
    }

    @Test
    fun publicRoutesBypassPrivateLanAddresses() {
        val routes = VpnRoutePolicy.publicIpv4RoutesExcludingPrivateLan

        assertFalse(VpnRoutePolicy.contains(routes, "10.0.0.1"))
        assertFalse(VpnRoutePolicy.contains(routes, "172.16.0.1"))
        assertFalse(VpnRoutePolicy.contains(routes, "172.31.255.254"))
        assertFalse(VpnRoutePolicy.contains(routes, "192.168.3.1"))
        assertFalse(VpnRoutePolicy.contains(routes, "169.254.1.1"))
        assertFalse(VpnRoutePolicy.contains(routes, "127.0.0.1"))
    }
}
