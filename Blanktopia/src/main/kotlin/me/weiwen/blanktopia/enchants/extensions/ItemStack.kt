package me.weiwen.blanktopia.enchants.extensions

import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import java.util.*
import kotlin.random.Random

fun ItemStack.damage(damage: Int) {
    val meta = this.itemMeta ?: return
    val unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY)
    if (meta is Damageable && (1.0 / (unbreaking + 1)) > Random.nextDouble()) {
        meta.damage += damage
    }
    this.itemMeta = meta
}

fun ItemStack.setHeadHash(name: String, hash: String) {
    val url = "http://textures.minecraft.net/texture/$hash"
    setHeadUrl(name, url)
}

fun ItemStack.setHeadUrl(name: String, url: String) {
    val bytes = Base64.getEncoder().encode("{textures:{SKIN:{url:\"$url\"}}}".toByteArray())
    setHeadBase64(name, String(bytes))
}

fun ItemStack.setHeadBase64(name: String, base64: String) {
    val uuid = base64.hashCode()
    Bukkit.getUnsafe().modifyItemStack(
        this,
        "{SkullOwner:{Name:\"$name\",Id:[I;-1,$uuid,-1,$uuid],Properties:{textures:[{Value:\"$base64\"}]}}}"
    )
}
