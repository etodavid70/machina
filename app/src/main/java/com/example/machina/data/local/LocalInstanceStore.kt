package com.example.machina.data.local

import android.content.Context
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalInstanceStore(context: Context) {

    private val prefs = context.getSharedPreferences(
        "local_saved_servers",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    private val instanceListType =
        object : TypeToken<List<ServerInstance>>() {}.type

    fun getInstances(ownerEmail: String): List<ServerInstance> {
        val json = prefs.getString(storageKey(ownerEmail), null) ?: return emptyList()

        return runCatching {
            gson.fromJson<List<ServerInstance>>(json, instanceListType).orEmpty()
        }.getOrElse {
            emptyList()
        }
    }

    fun saveInstance(
        ownerEmail: String,
        savedServer: SavedServer
    ): ServerInstance {
        val instances = getInstances(ownerEmail)
        val now = System.currentTimeMillis().toString()

        val instance = ServerInstance(
            id = nextId(instances),
            name = savedServer.name,
            connectionType = savedServer.connectionType,
            username = savedServer.username,
            port = savedServer.port,
            publicIp = savedServer.publicIp,
            privateIp = savedServer.privateIp,
            serviceProvider = savedServer.serviceProvider,
            lastConnectedAt = savedServer.lastConnectedAt,
            createdAt = savedServer.createdAt ?: now,
            updatedAt = savedServer.updatedAt ?: now
        )

        writeInstances(ownerEmail, instances + instance)
        return instance
    }

    fun deleteInstance(ownerEmail: String, id: Int): Boolean {
        val instances = getInstances(ownerEmail)
        val updatedInstances = instances.filterNot { it.id == id }

        if (updatedInstances.size == instances.size) return false

        writeInstances(ownerEmail, updatedInstances)
        return true
    }

    private fun writeInstances(
        ownerEmail: String,
        instances: List<ServerInstance>
    ) {
        prefs.edit()
            .putString(storageKey(ownerEmail), gson.toJson(instances))
            .apply()
    }

    private fun storageKey(ownerEmail: String): String {
        require(ownerEmail.isNotBlank()) { "A logged-in user's email is required." }
        return "instances_${ownerEmail.trim().lowercase()}"
    }

    private fun nextId(instances: List<ServerInstance>): Int {
        val usedIds = instances.mapTo(mutableSetOf()) { it.id }
        var id = 1

        while (id in usedIds) {
            check(id != Int.MAX_VALUE) { "No local instance IDs are available." }
            id++
        }

        return id
    }
}