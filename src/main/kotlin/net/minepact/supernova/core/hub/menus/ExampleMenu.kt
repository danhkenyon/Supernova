package net.minepact.supernova.core.hub.menus

import net.minepact.supernova.api.menus.*
import net.minepact.supernova.api.menus.component.*
import net.minepact.supernova.api.menus.pages.*
import net.minepact.supernova.api.message.MessageBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ExampleMenu : Menu(
    title = MessageBuilder()
        .append("&8Example Menu")
        .build(),
    size = 27
) {
    init {
    }

    override fun render(session: MenuSession) {
        session.set(SimpleMenuComponent(
                slot = 13,
                item = { ItemStack(Material.DIAMOND) },
                click = { session, _ ->
                    session.player.sendMessage("You clicked the diamond!")
                    session.player.closeInventory()
                }
        ))
        session.set(SimpleMenuComponent(
                slot = 22,
                item = { ItemStack(Material.BARRIER) },
                click = { session, _ ->
                    MenuManager.close(session.player)
                }
        ))
    }

    override fun onOpen(session: MenuSession) {
        session.player.sendMessage("You opened the example menu!")

        val pages = listOf(
            ComponentMenuPage(
                listOf(
                    SimpleMenuComponent(
                        slot = 10,
                        item = { session -> ItemStack(Material.DIAMOND) },
                        click = { session, event -> null }
                    ),
                    SimpleMenuComponent(
                        slot = 11,
                        item = { session -> ItemStack(Material.EMERALD) },
                        click = { session, event -> null }
                    ),
                )
            ),

            ComponentMenuPage(
                listOf(
                    SimpleMenuComponent(
                        slot = 10,
                        item = { session -> ItemStack(Material.GOLD_INGOT) },
                        click = { session, event -> null }
                    ),
                    SimpleMenuComponent(
                        slot = 11,
                        item = { session -> ItemStack(Material.IRON_INGOT) },
                        click = { session, event -> null }
                    ),
                )
            ),

            ComponentMenuPage(
                listOf(
                    SimpleMenuComponent(
                        slot = 10,
                        item = { session -> ItemStack(Material.NETHERITE_INGOT) },
                        click = { session, event -> null }
                    ),
                )
            )
        )

        session.paginate(
            ListMenuPageProvider(pages)
        )

        session.set(NextPageComponent(26))
    }

    override fun onClose(session: MenuSession) {
        session.player.sendMessage("You closed the example menu!")
    }
}