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
}