package com.unicorn.ticket.bs.ui.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.unicorn.ticket.bs.ui.SystemFra
import com.unicorn.ticket.bs.ui.OrderSearchFra
import com.unicorn.ticket.bs.ui.SellTicketFra
import com.unicorn.ticket.bs.ui.StatFra

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    companion object {
        val titles = listOf("售票", "票单查询", "当日总汇", "系统设置")
    }

    override fun getItem(position: Int) = when (position) {
        0 -> SellTicketFra()
        1 -> OrderSearchFra()
        2 -> StatFra()
        else -> SystemFra()
    }

    override fun getCount() = titles.size

}