package net.minepact.supernova.api.menus

abstract class Menu {
    abstract val id: String
    abstract val title: String
    abstract val rows: Int

    open fun build(context: MenuContext, layout: MenuLayout) {}

    open fun onOpen(context: MenuContext) {}
    open fun onClose(context: MenuContext) {}
    open fun onDestroy(context: MenuContext) {}
    open fun onClick(context: MenuContext, slot: Int) {}
    open fun onDrag(context: MenuContext, slots: Set<Int>) {}

    fun validate() {
        require(id.isNotBlank()) { "Menu ID cannot be blank" }
        require(rows in 1..6) { "Menu rows must be between 1 and 6" }
    }
}