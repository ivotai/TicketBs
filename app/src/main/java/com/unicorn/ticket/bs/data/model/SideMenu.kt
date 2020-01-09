package com.unicorn.ticket.bs.data.model

import androidx.annotation.DrawableRes
import com.unicorn.ticket.bs.R

enum class SideMenu(
    val text: String,
    @DrawableRes val resId: Int

) {
    SellTicket(
        "售票",
        R.mipmap.menu_sell_ticket
    ),
    OrderSearch(
        "票单查询",
        R.mipmap.menu_team_appoint
    ),
    ReportStatistics(
        "当日总汇",
        R.mipmap.menu_report_statistics
    ),
    SystemSetting(
        "系统设置",
        R.mipmap.menu_system_setting
    )
    ;
}

