package com.example.machina.data.model.dashboard_models

data class SshConnectionRequest(
    val host: String,
    val username: String,
    val port: Int = 22,
    val password: String? = null,
    val privateKey: ByteArray? = null,
    val privateKeyName: String? = null
) {
    init {
        require(host.isNotBlank()) { "Public IP address is required" }
        require(username.isNotBlank()) { "Username is required" }
        require(port in 1..65535) { "Port must be between 1 and 65535" }
        require(!password.isNullOrBlank() || privateKey?.isNotEmpty() == true) {
            "Enter a password or upload a PEM key"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SshConnectionRequest

        if (port != other.port) return false
        if (host != other.host) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (!privateKey.contentEquals(other.privateKey)) return false
        if (privateKeyName != other.privateKeyName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = port
        result = 31 * result + host.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (privateKey?.contentHashCode() ?: 0)
        result = 31 * result + (privateKeyName?.hashCode() ?: 0)
        return result
    }
}

data class SshConnectionResult(
    val host: String,
    val username: String,
    val port: Int,
    val output: String
)
