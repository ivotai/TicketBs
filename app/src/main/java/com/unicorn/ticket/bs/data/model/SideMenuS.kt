package com.unicorn.ticket.bs.data.model

data class SideMenuS(
    val sideMenu: SideMenu,
    var isSelected: Boolean = false
) {
    companion object {
        val all
            get() = listOf(
                SideMenuS(SideMenu.SellTicket, true),
                SideMenuS(SideMenu.OrderSearch),
                SideMenuS(SideMenu.ReportStatistics),
                SideMenuS(SideMenu.SystemSetting)
            )
    }
}


