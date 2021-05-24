package me.weiwen.blanktopia.conditions

import me.weiwen.blanktopia.hasBuildTrust
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HasBuildTrustCondition(private val material: Material) : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.hasBuildTrust(player.location, material)
    }
}