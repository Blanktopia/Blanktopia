package me.weiwen.blanktopia.storage

import java.util.*

data class PlayerData(
    val uuid: UUID,
    var hasSpawnedDragon: Boolean = false,
    var seenTutorials: MutableSet<String> = mutableSetOf()
)

interface IStorage {
    suspend fun save()
    suspend fun enable()
    suspend fun disable()

    suspend fun savePlayer(uuid: UUID, data: PlayerData)
    suspend fun loadPlayer(uuid: UUID): PlayerData

    suspend fun createLikes(uuid: UUID, world: String, x: Int, y: Int, z: Int, callback: (String) -> Unit)
    suspend fun like(uuid: UUID, id: String, callback: (Int) -> Unit)
    suspend fun unlike(uuid: UUID, id: String, callback: (Int) -> Unit)
}