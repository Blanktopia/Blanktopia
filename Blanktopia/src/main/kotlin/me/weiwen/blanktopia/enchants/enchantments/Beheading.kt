package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.AXES
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.playerHeadFromTexture
import me.weiwen.blanktopia.playerHeadFromUrl
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

val BEHEADING = CustomEnchantment(
    "beheading",
    "Beheading",
    5,
    AXES + BOOKS,
    NONE,
    0.1,
    10,
    10,
    3,
    { setOf() },
    Beheading
)

object Beheading : Listener {
    init {}

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity: Entity = event.entity
        val lastDamageCause = entity.lastDamageCause
        if (lastDamageCause != null) {
            if (lastDamageCause.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && lastDamageCause.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        }
        val killer = event.entity.killer
        if (killer != null) {
            val weapon = killer.equipment?.itemInMainHand ?: return
            if (weapon.containsEnchantment(BEHEADING)) {
                if (entity is Player) {
                    val skull = ItemStack(Material.PLAYER_HEAD)
                    val skullMeta = skull.itemMeta as SkullMeta
                    skullMeta.setOwningPlayer(entity)
                    skullMeta.setDisplayName(ChatColor.YELLOW.toString() + entity.name + "'s Head")
                    skullMeta.lore =
                        listOf(ChatColor.RESET.toString() + "Killed by " + ChatColor.RED + killer.name)
                    skull.itemMeta = skullMeta
                    event.drops.add(skull)
                    return
                }
                val (chance: Double, skull: ItemStack?) = when (entity) {
                    is Bat -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Bat",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgyMGExMGRiMjIyZjY5YWMyMjE1ZDdkMTBkY2E0N2VlYWZhMjE1NTUzNzY0YTJiODFiYWZkNDc5ZTc5MzNkMSJ9fX0="
                        )
                    )
                    is Bee -> Pair(
                        1.0,
                        playerHeadFromTexture(
                            "Bee",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MzIyZjgzMWUzYzE2OGNmYmQzZTI4ZmU5MjUxNDRiMjYxZTc5ZWIzOWM3NzEzNDlmYWM1NWE4MTI2NDczIn19fQ=="
                        )
                    )
                    is Blaze -> Pair(
                        0.001,
                        playerHeadFromTexture(
                            "Blaze",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ=="
                        )
                    )
                    is Cat -> Pair(
                        0.14, when (entity.catType) {
                            Cat.Type.TABBY -> playerHeadFromTexture(
                                "Tabby Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUyOGQzMGRiM2Y4YzNmZTUwY2E0ZjI2ZjMwNzVlMzZmMDAzYWU4MDI4MTM1YThjZDY5MmYyNGM5YTk4YWUxYiJ9fX0="
                            )
                            Cat.Type.BLACK -> playerHeadFromTexture(
                                "Tuxedo Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZkMTBjOGU3NWY2NzM5OGM0NzU4N2QyNWZjMTQ2ZjMxMWMwNTNjYzVkMGFlYWI4NzkwYmNlMzZlZTg4ZjVmOCJ9fX0="
                            )
                            Cat.Type.RED -> playerHeadFromTexture(
                                "Red Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzczYzk0ZWU0ZmM1ODAyZDEyNDkzYmJjZGVjNWI5ZWNlN2FkOTgxOTNlZDZlOTk5ODliNTZiNzM1NDIxYTdmMSJ9fX0="
                            )
                            Cat.Type.SIAMESE -> playerHeadFromTexture(
                                "Siamese Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk0MGFiNmM1NmU2ODUzY2ZmOGEzNWZjNTlkYmMzYWE0NTg5NGVmNDkyYTI3NmI2YTRmNTIwMzgwZjAyZjExOCJ9fX0="
                            )
                            Cat.Type.BRITISH_SHORTHAIR -> playerHeadFromTexture(
                                "British Shorthair Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E1NDRmYTA3MDIxMjI0MmU2NTc5MDk1NDhjZGFjODI3ODQyODM2YWE0ODZlZGYzMDRkMDM3OWNmMzJiNmNlYyJ9fX0="
                            )
                            Cat.Type.CALICO -> playerHeadFromTexture(
                                "Calico Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E1NDRmYTA3MDIxMjI0MmU2NTc5MDk1NDhjZGFjODI3ODQyODM2YWE0ODZlZGYzMDRkMDM3OWNmMzJiNmNlYyJ9fX0="
                            )
                            Cat.Type.PERSIAN -> playerHeadFromTexture(
                                "Persian Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRkZTZkYzc2M2E0NjRiOTZjYjMzYjllNWUzMGQ1ZTE4ZWVmOGNlYTA4NjIwOWQ3ODNmNzVkZTc5NzQ2NDNjOSJ9fX0="
                            )
                            Cat.Type.RAGDOLL -> playerHeadFromTexture(
                                "Ragdoll Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzlkZWQ2N2Q2Nzg5ZTBkODU0MmZlYWI4ZTFiZTUyYjk3YjA4Mzc2OTM4NWJiNjBmZTk1OGFhMzE0ZWVlNzQwMCJ9fX0="
                            )
                            Cat.Type.WHITE -> playerHeadFromTexture(
                                "White Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFkMTVhYzk1NThlOThiODlhY2E4OWQzODE5NTAzZjFjNTI1NmMyMTk3ZGQzYzM0ZGY1YWFjNGQ3MmU3ZmJlZCJ9fX0="
                            )
                            Cat.Type.JELLIE -> playerHeadFromTexture(
                                "Jellie",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM3ZDk0MGRhMTQxZDM2NzZjY2RlZTJiNzBkNWE4ZjUwYWYzZmIyOTA0ZGJlZjhlZWQ1YzhlOTI3ZDVmOTM0NiJ9fX0="
                            )
                            Cat.Type.ALL_BLACK -> playerHeadFromTexture(
                                "Black Cat",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UwYzY3ZTFjN2FlMDcxOGExM2Q4MTZjZTgzYmEwZjcxYjdkY2VkMGFjZjNlOTYyYzIwM2Q1MDA1YTJkMmUyNSJ9fX0="
                            )
                        }
                    )
                    is Panda -> Pair(
                        0.018, when (entity.mainGene) {
                            Panda.Gene.AGGRESSIVE -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODNmZTFlNzgyYWU5NmEzMDMzNmEwM2VmNzQ2ODFjZTNhNjkwNWZjYzY3M2ZhNTZjMDQ2YWFlZTZhYTI4MzA3ZCJ9fX0="
                            )
                            Panda.Gene.BROWN -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzVkMGQ0NWJmOTkyYjA3MmNmNWM1MTNlMDZiZWVmZThiZGM4MDljODE1MGYzZDE0Yjg4Mzc5NmE3Yjc0ZTQwNiJ9fX0="
                            )
                            Panda.Gene.LAZY -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgxOGI2ODFjYWNlMWM2NDE5MTlmNTNlZGFkZWNiMTQyMzMwZDA4OWE4MjZiNTYyMTkxMzhjMzNiN2E1ZTBkYiJ9fX0="
                            )
                            Panda.Gene.NORMAL -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNhMDk2ZWVhNTA2MzAxYmVhNmQ0YjE3ZWUxNjA1NjI1YTZmNTA4MmM3MWY3NGE2MzljYzk0MDQzOWY0NzE2NiJ9fX0="
                            )
                            Panda.Gene.WORRIED -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhlOTIxYzU3ZTU0ZGQwNzMzN2ZmYmE2MjllNzJjYWYzODUwZDgzNmI3NjU2MmIxYmMzYmM1OTQ5ZjJkNDFkIn19fQ=="
                            )
                            Panda.Gene.PLAYFUL -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY0NjNlNjRjZTI5NzY0ZGIzY2I0NjgwNmNlZTYwNmFmYzI0YmRmMGNlMTRiNjY2MGMyNzBhOTZjNzg3NDI2In19fQ=="
                            )
                            Panda.Gene.WEAK -> playerHeadFromTexture(
                                "Panda",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWMyZDI1ZTk1NjMzN2Q4Mjc5MWZhMGU2NjE3YTQwMDg2ZjAyZDZlYmZiZmQ1YTY0NTk4ODljZjIwNmZjYTc4NyJ9fX0="
                            )
                        }
                    )
                    is Chicken -> Pair(
                        0.005,
                        playerHeadFromTexture(
                            "Chicken",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ=="
                        )
                    )
                    is Cow -> {
                        when (entity) {
                            is MushroomCow -> Pair(
                                0.002, playerHeadFromTexture(
                                    "Mooshroom",
                                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ=="
                                )
                            )
                            else -> Pair(
                                0.005, playerHeadFromTexture(
                                    "Cow",
                                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19"
                                )
                            )
                        }
                    }
                    is Creeper -> if (entity.isPowered) {
                        Pair(1.0, playerHeadFromUrl("Charged Creeper", "f2ceb39dd4de24a7adfe291a3a0cfc7cf4f645de59b603fcfe06c6b5a06e26"))
                    } else {
                        Pair(0.1, ItemStack(Material.CREEPER_HEAD))
                    }
                    is Dolphin -> Pair(
                        0.25,
                        playerHeadFromTexture(
                            "Dolphin",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="
                        )
                    )
                    is Donkey -> Pair(
                        0.09,
                        playerHeadFromTexture(
                            "Donkey",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZiNmMzYzA1MmNmNzg3ZDIzNmEyOTE1ZjgwNzJiNzdjNTQ3NDk3NzE1ZDFkMmY4Y2JjOWQyNDFkODhhIn19fQ=="
                        )
                    )
                    is EnderDragon -> Pair(1.0, ItemStack(Material.DRAGON_HEAD))
                    is Enderman -> Pair(
                        0.0002,
                        playerHeadFromTexture(
                            "Enderman",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0="
                        )
                    )
                    is Endermite -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Endermite",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="
                        )
                    )
                    is Evoker -> Pair(
                        0.14,
                        playerHeadFromTexture(
                            "Evoker",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ=="
                        )
                    )
                    is Fish -> when (entity) {
                        is Cod -> Pair(
                            0.1,
                            playerHeadFromTexture(
                                "Cod",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0="
                            )
                        )
                        is PufferFish -> Pair(
                            0.15,
                            playerHeadFromTexture(
                                "Pufferfish",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxNTI4NzZiYzNhOTZkZDJhMjI5OTI0NWVkYjNiZWVmNjQ3YzhhNTZhYzg4NTNhNjg3YzNlN2I1ZDhiYiJ9fX0="
                            )
                        )
                        is Salmon -> Pair(
                            0.1,
                            playerHeadFromTexture(
                                "Salmon",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWRmYzU3ZDA5MDU5ZTQ3OTlmYTkyYzE1ZTI4NTEyYmNmYWExMzE1NTc3ZmUzYTI3YWVkMzg5ZTRmNzUyMjg5YSJ9fX0="
                            )
                        )
                        is TropicalFish -> Pair(
                            0.1,
                            playerHeadFromTexture(
                                "Tropical Fish",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZkMTQ5ZTRkNDk5OTI5NjcyZTI3Njg5NDllNjQ3Nzk1OWMyMWU2NTI1NDYxM2IzMjdiNTM4ZGYxZTRkZiJ9fX0"
                            )
                        )
                        else -> Pair(0.0, null)
                    }
                    is Fox -> Pair(
                        0.018, when (entity.foxType) {
                            Fox.Type.RED -> playerHeadFromTexture(
                                "Fox",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ=="
                            )
                            Fox.Type.SNOW -> playerHeadFromTexture(
                                "Fox",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE0MzYzNzdlYjRjNGI0ZTM5ZmIwZTFlZDg4OTlmYjYxZWUxODE0YTkxNjliOGQwODcyOWVmMDFkYzg1ZDFiYSJ9fX0="
                            )
                        }
                    )
                    is Ghast -> Pair(
                        0.025,
                        playerHeadFromTexture(
                            "Ghast",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19"
                        )
                    )
                    is Giant -> Pair(1.0, ItemStack(Material.ZOMBIE_HEAD))
                    is Guardian -> when (entity) {
                        is ElderGuardian -> Pair(
                            0.3, playerHeadFromTexture(
                                "Elder Guardian",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19"
                            )
                        )
                        else -> Pair(
                            0.002, playerHeadFromTexture(
                                "Guardian",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ=="
                            )
                        )
                    }
                    is Horse -> Pair(
                        0.02, when (entity.color) {
                            Horse.Color.BLACK -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiN2ZjNWY3YTlkZGZkZDFhYTc5MzE3NDEwZmMxOTI5ZjkxYmRkZjk4NTg1OTM4YTJhNTYxOTlhNjMzY2MifX19"
                            )
                            Horse.Color.WHITE -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzODEzZGQ0NWVkMGVmODM4NDQ4Y2Y2ZjYzMWMxNTdjMjNmOTY1MGM1YWU0NTFlOTc4YTUzMzgzMzEyZmUifX19"
                            )
                            Horse.Color.CREAMY -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JiNGIyODg5OTFlZmI4Y2EwNzQzYmVjY2VmMzEyNThiMzFkMzlmMjQ5NTFlZmIxYzljMThhNDE3YmE0OGY5In19fQ=="
                            )
                            Horse.Color.CHESTNUT -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JiNGIyODg5OTFlZmI4Y2EwNzQzYmVjY2VmMzEyNThiMzFkMzlmMjQ5NTFlZmIxYzljMThhNDE3YmE0OGY5In19fQ=="
                            )
                            Horse.Color.BROWN -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJlYjk2N2FiOTRmZGQ0MWE2MzI1ZjEyNzdkNmRjMDE5MjI2ZTVjZjM0OTc3ZWVlNjk1OTdmYWZjZjVlIn19fQ=="
                            )
                            Horse.Color.GRAY -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzODEzZGQ0NWVkMGVmODM4NDQ4Y2Y2ZjYzMWMxNTdjMjNmOTY1MGM1YWU0NTFlOTc4YTUzMzgzMzEyZmUifX19"
                            )
                            Horse.Color.DARK_BROWN -> playerHeadFromTexture(
                                "Horse",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJlYjk2N2FiOTRmZGQ0MWE2MzI1ZjEyNzdkNmRjMDE5MjI2ZTVjZjM0OTc3ZWVlNjk1OTdmYWZjZjVlIn19fQ=="
                            )
                        }
                    )
                    is Illusioner -> Pair(
                        0.14,
                        playerHeadFromTexture(
                            "Illusioner",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNTEyZTdkMDE2YTIzNDNhN2JmZjFhNGNkMTUzNTdhYjg1MTU3OWYxMzg5YmQ0ZTNhMjRjYmViODhiIn19fQ=="
                        )
                    )
                    is IronGolem -> Pair(
                        0.055,
                        playerHeadFromTexture(
                            "Iron Golem",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19"
                        )
                    )
                    is Llama -> Pair(
                        0.04,
                        playerHeadFromTexture(
                            "Llama",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc3NmE3OGY5NjI0NGUzZGE3MzJmYWZmZDkzYTMzOTgzNGRiMjdiNjk1NWJmN2E5YjI0YWU5ODEyNWI3ZWQifX19"
                        )
                    )
                    is MagmaCube -> Pair(
                        0.005,
                        playerHeadFromTexture(
                            "Magma Cube",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0="
                        )
                    )
                    is Ocelot -> Pair(
                        0.04,
                        playerHeadFromTexture(
                            "Ocelot",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19"
                        )
                    )
                    is Parrot -> Pair(
                        1.0, when (entity.variant) {
                            Parrot.Variant.BLUE -> playerHeadFromTexture(
                                "Parrot",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZTFjNWY0OGE3ZTEyYjI2Mjg1MzU3MWVmMWY1OTdhOTJlZjU4ZGE4ZmFhZmUwN2JiN2MwZTY5ZTkzIn19fQ=="
                            )
                            Parrot.Variant.RED -> playerHeadFromTexture(
                                "Parrot",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRiYThkNjZmZWNiMTk5MmU5NGI4Njg3ZDZhYjRhNTMyMGFiNzU5NGFjMTk0YTI2MTVlZDRkZjgxOGVkYmMzIn19fQ=="
                            )
                            Parrot.Variant.GREEN -> playerHeadFromTexture(
                                "Parrot",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWI5YTM2YzU1ODlmM2EyZTU5YzFjYWE5YjNiODhmYWRhNzY3MzJiZGI0YTc5MjYzODhhOGMwODhiYmJjYiJ9fX0="
                            )
                            Parrot.Variant.CYAN -> playerHeadFromTexture(
                                "Parrot",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI5NGYyMzZjNGE2NDJlYjJiY2RjMzU4OWI5YzNjNGEwYjViZDVkZjljZDVkNjhmMzdmOGM4M2Y4ZTNmMSJ9fX0="
                            )
                            Parrot.Variant.GRAY -> playerHeadFromTexture(
                                "Parrot",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Q2ZjRhMjFlMGQ2MmFmODI0Zjg3MDhhYzYzNDEwZjFhMDFiYmI0MWQ3ZjRhNzAyZDk0NjljNjExMzIyMiJ9fX0="
                            )
                        }
                    )
                    is Pig -> Pair(
                        0.005,
                        playerHeadFromTexture(
                            "Pig",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0="
                        )
                    )
                    is Pillager -> Pair(
                        0.035,
                        playerHeadFromTexture(
                            "Pillager",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0="
                        )
                    )
                    is PolarBear -> Pair(
                        0.1,
                        playerHeadFromTexture(
                            "Polar Bear",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q1ZDYwYTRkNzBlYzEzNmE2NTg1MDdjZTgyZTM0NDNjZGFhMzk1OGQ3ZmNhM2Q5Mzc2NTE3YzdkYjRlNjk1ZCJ9fX0="
                        )
                    )
                    is Phantom -> Pair(
                        0.1,
                        playerHeadFromTexture(
                            "Phantom",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4MiJ9fX0="
                        )
                    )
                    is Rabbit -> Pair(
                        0.1,
                        playerHeadFromTexture(
                            "Rabbit",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlY2M2YjVlNmVhNWNlZDc0YzQ2ZTc2MjdiZTNmMDgyNjMyN2ZiYTI2Mzg2YzZjYzc4NjMzNzJlOWJjIn19fQ=="
                        )
                    )
                    is Ravager -> Pair(
                        0.14,
                        playerHeadFromTexture(
                            "Ravager",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I2MjUwMWNkMWI4N2IzN2Y2MjgwMTgyMTBlYzU0MDBjYjY1YTRkMWFhYjc0ZTZhM2Y3ZjYyYWE4NWRiOTdlZSJ9fX0="
                        )
                    )
                    is Sheep -> Pair(
                        0.0175, when (entity.color) {
                            DyeColor.ORANGE -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA5ODM5N2EyNzBiNGMzZDJiMWU1NzRiOGNmZDNjYzRlYTM0MDkwNjZjZWZlMzFlYTk5MzYzM2M5ZDU3NiJ9fX0="
                            )
                            DyeColor.MAGENTA -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTgzNjU2NWM3ODk3ZDQ5YTcxYmMxODk4NmQxZWE2NTYxMzIxYTBiYmY3MTFkNDFhNTZjZTNiYjJjMjE3ZTdhIn19fQ=="
                            )
                            DyeColor.LIGHT_BLUE -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM4YTk3YTM4ODU2NTE0YTE2NDEzZTJjOTk1MjEyODlmNGM1MzYzZGM4MmZjOWIyODM0Y2ZlZGFjNzhiODkifX19"
                            )
                            DyeColor.YELLOW -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjZhNDExMmRmMWU0YmNlMmE1ZTI4NDE3ZjNhYWZmNzljZDY2ZTg4NWMzNzI0NTU0MTAyY2VmOGViOCJ9fX0="
                            )
                            DyeColor.LIME -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTJhMjQ0OGY1OGE0OTEzMzI0MzRlODVjNDVkNzg2ZDg3NDM5N2U4MzBhM2E3ODk0ZTZkOTI2OTljNDJiMzAifX19"
                            )
                            DyeColor.PINK -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFjNzRhMmI5YjkxNDUyZTU2ZmExZGRhNWRiODEwNzc4NTZlNDlmMjdjNmUyZGUxZTg0MWU1Yzk1YTZmYzVhYiJ9fX0="
                            )
                            DyeColor.GRAY -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI4N2ViNTAxMzkxZjI3NTM4OWYxNjZlYzlmZWJlYTc1ZWM0YWU5NTFiODhiMzhjYWU4N2RmN2UyNGY0YyJ9fX0="
                            )
                            DyeColor.LIGHT_GRAY -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UxYWM2ODM5OTNiZTM1NTEyZTFiZTMxZDFmNGY5OGU1ODNlZGIxNjU4YTllMjExOTJjOWIyM2I1Y2NjZGMzIn19fQ=="
                            )
                            DyeColor.CYAN -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZmNmM3ZTdmZDUxNGNlMGFjYzY4NTkzMjI5ZTQwZmNjNDM1MmI4NDE2NDZlNGYwZWJjY2NiMGNlMjNkMTYifX19"
                            )
                            DyeColor.PURPLE -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWU1Mjg2N2FmZWYzOGJiMTRhMjZkMTQyNmM4YzBmMTE2YWQzNDc2MWFjZDkyZTdhYWUyYzgxOWEwZDU1Yjg1In19fQ=="
                            )
                            DyeColor.BLUE -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllYzIyODE4ZDFmYmZjODE2N2ZiZTM2NzI4YjI4MjQwZTM0ZTE2NDY5YTI5MjlkMDNmZGY1MTFiZjJjYTEifX19"
                            )
                            DyeColor.BROWN -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU1YWQ2ZTVkYjU2OTJkODdmNTE1MTFmNGUwOWIzOWZmOWNjYjNkZTdiNDgxOWE3Mzc4ZmNlODU1M2I4In19fQ=="
                            )
                            DyeColor.GREEN -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAxNzIxNWM3ZjhkYjgyMDQwYWEyYzQ3Mjk4YjY2NTQxYzJlYjVmN2Y5MzA0MGE1ZDIzZDg4ZjA2ODdkNGIzNCJ9fX0="
                            )
                            DyeColor.RED -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM5YWY0NzdlYjYyNzgxNWY3MjNhNTY2MjU1NmVjOWRmY2JhYjVkNDk0ZDMzOGJkMjE0MjMyZjIzZTQ0NiJ9fX0="
                            )
                            DyeColor.BLACK -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzI2NTIwODNmMjhlZDFiNjFmOWI5NjVkZjFhYmYwMTBmMjM0NjgxYzIxNDM1OTUxYzY3ZDg4MzY0NzQ5ODIyIn19fQ=="
                            )
                            else -> playerHeadFromTexture(
                                "Sheep",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19"
                            )
                        }
                    )
                    is Shulker -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Shulker",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjQyY2ZlMzM4MTQ3NjdkNjkyZDI3NTk5YzhiZWY0ZjVhMzA2ZmMyMTBkNGI1MGE1ODBiNzA0MGYwMmIxOCJ9fX0="
                        )
                    )
                    is Silverfish -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Silverfish",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTJlYzJjM2NiOTVhYjc3ZjdhNjBmYjRkMTYwYmNlZDRiODc5MzI5YjYyNjYzZDdhOTg2MDY0MmU1ODhhYjIxMCJ9fX0="
                        )
                    )
                    is Skeleton -> {
                        when (entity) {
                            is Stray -> Pair(0.04, playerHeadFromTexture(
                                "Stray",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ=="
                            ))
                            is WitherSkeleton -> Pair(0.01, ItemStack(Material.WITHER_SKELETON_SKULL))
                            else -> Pair(0.05, ItemStack(Material.SKELETON_SKULL))
                        }
                    }
                    is SkeletonHorse -> Pair(
                        0.2,
                        playerHeadFromTexture(
                            "Skeleton Horse",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ=="
                        )
                    )
                    is Slime -> Pair(
                        0.005,
                        playerHeadFromTexture(
                            "Slime",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk1YWVlYzZiODQyYWRhODY2OWY4NDZkNjViYzQ5NzYyNTk3ODI0YWI5NDRmMjJmNDViZjNiYmI5NDFhYmU2YyJ9fX0="
                        )
                    )
                    is Snowman -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Snow Golem",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0="
                        )
                    )
                    is Spider -> {
                        when (entity) {
                            is CaveSpider -> Pair(0.01, playerHeadFromTexture(
                                "Cave Spider",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNkMThkYWQwYmQ5MGYwMmFjMDg3NTRhNzUzNThiMWMyN2RhNTc5YjNjYTM1YjY5ZTZiYjEwYTdhNWMyZGJkYSJ9fX0="
                            ))
                            else -> Pair(0.002, playerHeadFromTexture(
                                "Spider",
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM4ODE3M2Y0ZjgyYTE0MTUzZTA4NmJmMTM3OTA3MjU2ZTUxMmIyMTczMWYwNDcwMDQ3YmYyZDQ1MzU0NWQyMSJ9fX0="
                            ))
                        }
                    }
                    is Squid -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Squid",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDE0MzNiZTI0MjM2NmFmMTI2ZGE0MzRiODczNWRmMWViNWIzY2IyY2VkZTM5MTQ1OTc0ZTljNDgzNjA3YmFjIn19fQ=="
                        )
                    )
                    is Turtle -> Pair(
                        0.1,
                        playerHeadFromTexture(
                            "Turtle",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ=="
                        )
                    )
                    is Vex -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Vex",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3MzMwYzdkNWNkOGEwYTU1YWI5ZTk1MzIxNTM1YWM3YWUzMGZlODM3YzM3ZWE5ZTUzYmVhN2JhMmRlODZiIn19fQ=="
                        )
                    )
                    is Villager -> Pair(
                        1.0,
                        playerHeadFromTexture(
                            "Villager",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19"
                        )
                    )
                    is Vindicator -> Pair(
                        0.055,
                        playerHeadFromTexture(
                            "Vindicator",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRlYWVjMzQ0YWIwOTViNDhjZWFkNzUyN2Y3ZGVlNjFiMDYzZmY3OTFmNzZhOGZhNzY2NDJjODY3NmUyMTczIn19fQ=="
                        )
                    )
                    is WanderingTrader -> Pair(
                        1.0,
                        playerHeadFromTexture(
                            "Wandering Trader",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0="
                        )
                    )
                    is Witch -> Pair(
                        0.002,
                        playerHeadFromTexture(
                            "Witch",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0="
                        )
                    )
                    is Wither -> Pair(
                        1.0,
                        playerHeadFromTexture(
                            "Wither",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RmNzRlMzIzZWQ0MTQzNjk2NWY1YzU3ZGRmMjgxNWQ1MzMyZmU5OTllNjhmYmI5ZDZjZjVjOGJkNDEzOWYifX19"
                        )
                    )
                    is Wolf -> Pair(
                        0.02,
                        playerHeadFromTexture(
                            "Wolf",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc2OGNiZDUyMWNiMTg1YjI3NjcyY2U0MzBmYzQ4NTBkNzU5NWM2ZTg3NzhlMDcyNzFjOTU3OWVkMWY1YWZiNSJ9fX0="
                        )
                    )
                    is Zombie -> {
                        when (entity) {
                            is ZombieVillager ->
                                Pair(
                                    0.09,
                                    playerHeadFromTexture(
                                        "Zombie Villager",
                                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVlMDhhODc3NmMxNzY0YzNmZTZhNmRkZDQxMmRmY2I4N2Y0MTMzMWRhZDQ3OWFjOTZjMjFkZjRiZjNhYzg5YyJ9fX0="
                                    )
                                )
                            is PigZombie -> Pair(
                                0.001,
                                playerHeadFromTexture(
                                    "Zombified Piglin",
                                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkzNTg0MmFmNzY5MzgwZjc4ZThiOGE4OGQxZWE2Y2EyODA3YzFlNTY5M2MyY2Y3OTc0NTY2MjA4MzNlOTM2ZiJ9fX0="
                                )
                            )
                            is Drowned -> Pair(
                                0.04,
                                playerHeadFromTexture(
                                    "Drowned",
                                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0="
                                )
                            )
                            is Husk -> Pair(
                                0.03,
                                playerHeadFromTexture(
                                    "Husk",
                                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19"
                                )
                            )
                            else -> Pair(0.05, ItemStack(Material.ZOMBIE_HEAD))
                        }
                    }
                    is ZombieHorse -> Pair(
                        1.0,
                        playerHeadFromTexture(
                            "Zombie Horse",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWI1NGYyYmMyYjZmNTlhYmI4YzNmOWQ2YzNhMzE1NGUzZmVmZTIzOGYyYmFkNTkzZGIyZGU3ZmEzYTQifX19"
                        )
                    )

                    // 1.16
                    is Hoglin -> Pair(
                        0.05,
                        playerHeadFromTexture(
                            "Hoglin",
                            "9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75"
                        )
                    )
                    is Piglin -> Pair(
                        0.05,
                        playerHeadFromUrl(
                            "Piglin",
                            "5081a1239fffe135cbfa4a98a6aa6cc5b0787ad0790f56a16bf07f86374606c5"
                        )
                    )
                    is Strider -> Pair(
                        0.1,
                        playerHeadFromUrl(
                            "Strider",
                            "18a9adf780ec7dd4625c9c0779052e6a15a451866623511e4c82e9655714b3c1"
                        )
                    )
                    is Zoglin -> Pair(
                        0.05,
                        playerHeadFromUrl(
                            "Zoglin",
                            "e67e18602e03035ad68967ce090235d8996663fb9ea47578d3a7ebbc42a5ccf9"
                        )
                    )
                    else -> Pair(0.0, null)
                }
                if (skull != null && weapon.getEnchantmentLevel(BEHEADING) * chance > Math.random()) {
                    event.drops.add(skull)
                }
            }
        }
    }
}
