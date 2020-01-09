package com.unicorn.ticket.bs.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.data.model.TicketSaleStatInfo
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.item_ticket_sale_stat.*

class TicketSaleStatAdapter :
    BaseQuickAdapter<TicketSaleStatInfo, KVHolder>(R.layout.item_ticket_sale_stat) {

    override fun convert(helper: KVHolder, item: TicketSaleStatInfo) {
        helper.apply {
            tvProductName.text = item.product_name
            tvQuantity.text = item.quantity
            tvTotalAmount.text = item.total_amount.toString()
        }
    }

}