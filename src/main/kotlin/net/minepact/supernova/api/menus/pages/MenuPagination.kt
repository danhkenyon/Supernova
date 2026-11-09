package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.MenuSession

class MenuPagination(
    private val session: MenuSession,
    private val provider: MenuPageProvider
) {
    var currentPage: Int = 0
        private set
    val pageCount: Int
        get() = provider.pageCount(session).coerceAtLeast(0)
    val hasPrevious: Boolean
        get() = currentPage > 0
    val hasNext: Boolean
        get() = currentPage < pageCount - 1
    val isFirstPage: Boolean
        get() = currentPage == 0
    val isLastPage: Boolean
        get() = currentPage >= pageCount - 1
    val current: MenuPage?
        get() = provider.getPage(session, currentPage)

    fun first(): Boolean = goTo(0)
    fun previous(): Boolean = goTo(currentPage - 1)
    fun next(): Boolean = goTo(currentPage + 1)
    fun last(): Boolean = goTo(pageCount - 1)

    fun goTo(index: Int): Boolean {
        if (index !in 0 until pageCount) return false
        if (index == currentPage && current != null) return false

        current?.onClose(session)
        currentPage = index
        session.refreshPage { current?.render(session) }
        current?.onOpen(session)

        return true
    }
}