package com.unicorn.ticket.bs.ui.adapter

import android.graphics.Color
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.ChangePagerEvent
import com.unicorn.ticket.bs.data.model.SideMenuS
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.item_side_menu_s.*

class SideMenuSAdapter : BaseQuickAdapter<SideMenuS, KVHolder>(R.layout.item_side_menu_s) {

    override fun convert(helper: KVHolder, item: SideMenuS) {
        helper.apply {
            tvText.text = item.sideMenu.text
            Glide.with(mContext).load(item.sideMenu.resId).into(ivRes)

            root.setBackgroundColor(Color.parseColor(if (item.isSelected) "#2D3238" else "#484F57"))
        }

        helper.root.safeClicks().subscribe {
            data.forEach { it.isSelected = false }
            item.isSelected = true
            notifyDataSetChanged()

            RxBus.post(ChangePagerEvent(data.indexOf(item)))
        }
    }

}