package me.weiwen.blanktopia.sql

import me.weiwen.blanktopia.BlanktopiaCore
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method


class NMSReflection {
    val CraftPlayer: Class<*>
    val CraftPlayer_getHandle: Method
    val EntityPlayer: Class<*>
    val EntityPlayer_playerConnection: Field

    val CraftChunk: Class<*>
    val CraftChunk_getHandle: Method
    val Chunk: Class<*>

    val PacketPlayOutMapChunk: Class<*>
    val PacketPlayOutMapChunk_constructor: Constructor<*>

    val PlayerConnection: Class<*>
    val PlayerConnection_sendPacket: Method

    init {
        val version = getNMSVersion() ?: "v1_16_R3"
        CraftPlayer = getNMSClass("org.bukkit.craftbukkit.%s.entity.CraftPlayer", version)
        CraftPlayer_getHandle = CraftPlayer.getDeclaredMethod("getHandle")
        EntityPlayer = getNMSClass("net.minecraft.server.%s.EntityPlayer", version)
        EntityPlayer_playerConnection = EntityPlayer.getDeclaredField("playerConnection")

        CraftChunk = getNMSClass("org.bukkit.craftbukkit.%s.CraftChunk", version)
        CraftChunk_getHandle = CraftChunk.getDeclaredMethod("getHandle")
        Chunk = getNMSClass("net.minecraft.server.%s.EntityChunk", version)

        PacketPlayOutMapChunk = getNMSClass("net.minecraft.server.%s.PacketPlayOutMapChunk", version)
        PacketPlayOutMapChunk_constructor = PacketPlayOutMapChunk.getConstructor(Chunk, Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)

        PlayerConnection = getNMSClass("net.minecraft.server.%s.PlayerConnection", version)
        PlayerConnection_sendPacket = PlayerConnection.getDeclaredMethod("sendPacket", PacketPlayOutMapChunk)

    }

    private fun getNMSVersion(): String? {
        val pat = Regex("org\\.bukkit\\.craftbukkit\\.(v(?:\\d_)+R\\d)")
        for (p in Package.getPackages()) {
            BlanktopiaCore.INSTANCE.logger.info(p.name)
            val match = pat.find(p.name)
            if (match != null) {
                return match.value
            }
        }
        return null
    }

    private fun getNMSClass(nmsClass: String, version: String): Class<*> {
        return Class.forName(String.format(nmsClass, version))
    }
}

