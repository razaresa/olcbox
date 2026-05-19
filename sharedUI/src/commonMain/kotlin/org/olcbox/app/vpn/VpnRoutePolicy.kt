package org.olcbox.app.vpn

internal data class Ipv4Route(
    val address: String,
    val prefixLength: Int
)

internal object VpnRoutePolicy {
    val publicIpv4RoutesExcludingPrivateLan: List<Ipv4Route> = listOf(
        "1.0.0.0/8",
        "2.0.0.0/7",
        "4.0.0.0/6",
        "8.0.0.0/7",
        "11.0.0.0/8",
        "12.0.0.0/6",
        "16.0.0.0/4",
        "32.0.0.0/3",
        "64.0.0.0/3",
        "96.0.0.0/4",
        "112.0.0.0/5",
        "120.0.0.0/6",
        "124.0.0.0/7",
        "126.0.0.0/8",
        "128.0.0.0/3",
        "160.0.0.0/5",
        "168.0.0.0/8",
        "169.0.0.0/9",
        "169.128.0.0/10",
        "169.192.0.0/11",
        "169.224.0.0/12",
        "169.240.0.0/13",
        "169.248.0.0/14",
        "169.252.0.0/15",
        "169.255.0.0/16",
        "170.0.0.0/7",
        "172.0.0.0/12",
        "172.32.0.0/11",
        "172.64.0.0/10",
        "172.128.0.0/9",
        "173.0.0.0/8",
        "174.0.0.0/7",
        "176.0.0.0/4",
        "192.0.0.0/9",
        "192.128.0.0/11",
        "192.160.0.0/13",
        "192.169.0.0/16",
        "192.170.0.0/15",
        "192.172.0.0/14",
        "192.176.0.0/12",
        "192.192.0.0/10",
        "193.0.0.0/8",
        "194.0.0.0/7",
        "196.0.0.0/6",
        "200.0.0.0/5",
        "208.0.0.0/4"
    ).map { it.toIpv4Route() }

    fun contains(routes: List<Ipv4Route>, ip: String): Boolean {
        val ipValue = ip.toIpv4Long()
        return routes.any { route ->
            val mask = route.prefixMask()
            ipValue and mask == route.address.toIpv4Long() and mask
        }
    }

    private fun String.toIpv4Route(): Ipv4Route {
        val parts = split("/")
        return Ipv4Route(parts[0], parts[1].toInt())
    }

    private fun Ipv4Route.prefixMask(): Long {
        return if (prefixLength == 0) {
            0L
        } else {
            (0xffffffffL shl (32 - prefixLength)) and 0xffffffffL
        }
    }

    private fun String.toIpv4Long(): Long {
        val octets = split(".")
        require(octets.size == 4) { "Invalid IPv4 address: $this" }
        return octets.fold(0L) { acc, octet ->
            val value = octet.toLong()
            require(value in 0..255) { "Invalid IPv4 address: $this" }
            (acc shl 8) or value
        }
    }
}
