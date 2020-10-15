package me.weiwen.blanktopia.nms

import net.minecraft.server.v1_16_R2.PacketPlayOutMapChunk
import net.minecraft.server.v1_16_R2.PacketPlayOutUnloadChunk
import org.bukkit.Chunk
import org.bukkit.craftbukkit.v1_16_R2.CraftChunk
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer
import org.bukkit.entity.Player

fun Chunk.send(player: Player) {
    val conn = (player as CraftPlayer).handle.playerConnection
    conn.sendPacket(PacketPlayOutUnloadChunk(this.x, this.z))
    conn.sendPacket(PacketPlayOutMapChunk((this as CraftChunk).handle, 65535))
}

//fun Chunk.send(player: Player) {
//    val nms = BlanktopiaCore.INSTANCE.nmsReflection
//    val craftPlayer = nms.CraftPlayer.cast(player)
//    val entityPlayer = nms.EntityPlayer.cast(nms.CraftPlayer_getHandle.invoke(craftPlayer))
//    val playerConnection = nms.PlayerConnection.cast(nms.EntityPlayer_playerConnection.get(entityPlayer))
//
//    val craftChunk = nms.CraftChunk.cast(this)
//    val entityChunk = nms.CraftChunk_getHandle.invoke(craftChunk)
//
//    val packet = nms.PacketPlayOutMapChunk_constructor.newInstance(entityChunk, 20, true)
//    nms.PlayerConnection_sendPacket.invoke(playerConnection, packet)
//}
