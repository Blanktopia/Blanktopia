package me.weiwen.blanktopia.items

import com.destroystokyo.paper.ParticleBuilder
import me.weiwen.blanktopia.Blanktopia
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class CustomItemAction(config: ConfigurationSection) {
    private var message: String? = config.getString("message")
    private var playerCommand: String? = config.getString("player-command")
    private var flyInClaims: Boolean? = config.getBoolean("fly-in-claims")
    private var portableBeacon: Boolean = config.getBoolean("portable-beacon")
    private var buildersWandBuild: Boolean = config.getBoolean("builders-wand-build")

    fun run(player: Player, block: Block?, face: BlockFace?) {
        message?.let { player.sendMessage(it) }
        playerCommand?.let { player.performCommand(it) }
        flyInClaims?.let {flyInClaims(player, it) }
        if (portableBeacon) portableBeacon(player)
        if (buildersWandBuild) buildersWandBuild(player, block, face)
    }
}

fun flyInClaims(player: Player, canFly: Boolean) {
    if (canFly) {
        player.persistentDataContainer.set(
            NamespacedKey(Blanktopia.INSTANCE, "fly-in-claims"),
            PersistentDataType.BYTE,
            1
        )
    } else {
        player.persistentDataContainer.remove(
            NamespacedKey(Blanktopia.INSTANCE, "fly-in-claims")
        )
        player.allowFlight = false
    }
}

fun portableBeacon(player: Player) {
    val haste = player.getPotionEffect(PotionEffectType.FAST_DIGGING)
    if (haste != null && haste.amplifier == 2 && haste.duration >= 10800) return
    val item = ItemStack(Material.NETHER_STAR, 1)
    if (player.inventory.containsAtLeast(item, 1)) {
        val world = player.location.world ?: return
        player.inventory.removeItem(item)
        player.sendMessage("A nether star disappears into the beacon.")
        for (other in world.getNearbyEntities(player.location, 64.0, 64.0, 64.0, { it is HumanEntity })) {
            if (other !is HumanEntity) continue
            world.playSound(other.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f)
            world.playSound(other.location, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f)
            world.spawnParticle(Particle.SPELL, other.location, 20)
            other.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 12000, 2, true, false))
            other.sendMessage(player.name + " has granted you " + ChatColor.AQUA + "Haste III" + ChatColor.RESET + " for " + ChatColor.AQUA + "10 minutes" + ChatColor.RESET + ".")
        }
    }
}

fun buildersWandBuild(player: Player, block: Block?, face: BlockFace?) {
    val range = 5;
    val size = 3;
}