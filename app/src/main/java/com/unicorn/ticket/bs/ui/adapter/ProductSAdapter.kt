package com.unicorn.ticket.bs.ui.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.model.ProductS
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.item_product_s.*

class ProductSAdapter : BaseQuickAdapter<ProductS, KVHolder>(R.layout.item_product_s) {

    override fun convert(helper: KVHolder, item: ProductS) {
        helper.apply {
            tvTitle.text = item.product.title
            tvPrice.text = "￥${item.product.price}"
            tvDescription.text = item.product.description

            root.background = ContextCompat.getDrawable(
                mContext,
                if (item.isSelected) R.mipmap.bg_ticket_on else R.mipmap.bg_ticket
            )
        }

        helper.root.safeClicks().subscribe {
            item.isSelected = !item.isSelected
            val index = data.indexOf(item)
//            item.index = index
            notifyItemChanged(index)

            RxBus.post(item)
        }

    }

}