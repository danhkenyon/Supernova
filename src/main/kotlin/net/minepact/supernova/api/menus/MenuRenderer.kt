package net.minepact.supernova.api.menus

import net.kyori.adventure.text.Component
import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.MessageTarget
import net.minepact.supernova.api.message.render.MessageRenderers
import org.bukkit.Bukkit

class MenuRenderer {
    fun render(context: MenuContext): MenuHolder {
        val menu = context.menu
        val layout = MenuLayout(menu.rows)

        menu.build(context, layout)
        context.menu.build(context, layout)

        val holder = MenuHolder(context, layout)
        val inventory = Bukkit.createInventory(
            holder,
            menu.rows * 9,
            MessageRenderers.render<Component>(
                MessageTarget.MENU_TITLE,
                MessageBuilder().append(menu.title).build()
            )
        )

        holder.attach(inventory)
        layout.entries().forEach { (slot, entry) ->
            inventory.setItem(slot, entry.item.clone().asItemStack())
        }

        return holder
    }
}