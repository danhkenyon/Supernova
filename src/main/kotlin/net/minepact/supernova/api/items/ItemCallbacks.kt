package net.minepact.supernova.api.items

import net.minepact.supernova.api.events.items.*

class ItemCallbacks internal constructor(
    val onClick: ((ItemClickEvent) -> Unit)? = null,
    val onLeftClick: ((ItemLeftClickEvent) -> Unit)? = null,
    val onRightClick: ((ItemRightClickEvent) -> Unit)? = null,
    val onDrop: ((ItemDropEvent) -> Unit)? = null,
    val onConsume: ((ItemConsumeEvent) -> Unit)? = null
)