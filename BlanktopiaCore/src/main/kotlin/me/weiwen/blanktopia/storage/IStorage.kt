package me.weiwen.blanktopia.storage

import java.util.*

data class PlayerData(
    val uuid: UUID,
    var hasSpawnedDragon: Boolean = false
)

interface IStorage {
    fun save()
    fun enable()
    fun disable()

    fun savePlayer(uuid: UUID, data: PlayerData)
    fun loadPlayer(uuid: UUID, callback: (PlayerData) -> Unit)
    fun getPlayerData(uuid: UUID): PlayerData?

    fun createLikes(uuid: UUID, world: String, x: Int, y: Int, z: Int, callback: (String) -> Unit)
    fun like(uuid: UUID, id: String, callback: (Int) -> Unit)
    fun unlike(uuid: UUID, id: String, callback: (Int) -> Unit)
}