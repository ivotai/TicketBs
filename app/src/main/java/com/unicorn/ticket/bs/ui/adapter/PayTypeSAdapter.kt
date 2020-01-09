package com.unicorn.ticket.bs.ui.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.model.PayTypeS
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.item_paytype_s.*

class PayTypeSAdapter : BaseQuickAdapter<PayTypeS, KVHolder>(R.layout.item_paytype_s) {

    var payFinish = false

    override fun convert(helper: KVHolder, item: PayTypeS) {
        helper.apply {
            tvPayment.text = item.payType.text
            Glide.with(mContext).load(item.payType.paymentResId).into(ivPayment)
            ivArrow.visibility = if (item.isSelect) View.VISIBLE else View.GONE
//            ivPayment.setImageResource(item.payType.paymentResId)
            root.alpha = if (item.isSelect) 1f else 0.4f
        }

        helper.root.safeClicks().subscribe {
            if (payFinish) return@subscribe
            data.forEach { it.isSelect = false }
            item.isSelect = true
            notifyDataSetChanged()
            RxBus.post(item)
        }
    }

}