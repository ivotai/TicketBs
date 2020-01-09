package com.unicorn.ticket.bs.ui.adapter

import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.data.model.TicketInfo
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.item_ticket_info.*

class TicketInfoAdapter : BaseQuickAdapter<TicketInfo, KVHolder>(R.layout.item_ticket_info) {

    override fun convert(helper: KVHolder, item: TicketInfo) {
        helper.apply {
            tvTitle.text =
                "${item.title} ${item.ticketNo} ${if (item.printStatus == 1) "已打印" else "未打印"}"
            GradientDrawable().apply {
                setStroke(1, ContextCompat.getColor(mContext, R.color.md_blue_600))
            }.let { tvTitle.background = it }
        }
    }

}